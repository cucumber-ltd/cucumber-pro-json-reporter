package io.cucumber.pro.results;

import io.cucumber.pro.Env;
import io.cucumber.pro.TestLogger;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static io.cucumber.pro.Env.CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS;
import static io.cucumber.pro.Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HTTPResultsPublisherTest {

    private Undertow server;

    @After
    public void stopServer() {
        if (server != null) server.stop();
    }

    @Test
    public void posts_results_as_multipart_formadata() throws InterruptedException {
        server = Undertow.builder()
                .addHttpListener(8082, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        if (exchange.isInIoThread()) {
                            exchange.dispatch(this);
                            return;
                        }
                        exchange.startBlocking();
                        FormParserFactory.Builder builder = FormParserFactory.builder();
                        FormDataParser formDataParser = builder.build().createParser(exchange);
                        FormData formData = formDataParser.parseBlocking();

                        assertEquals("README.md", formData.get("payload").getFirst().getFileName());
                        assertEquals("env.txt", formData.get("env").getFirst().getFileName());
                        assertEquals("the-profile", formData.get("profileName").getFirst().getValue());

                        exchange.getResponseSender().send("OK");
                    }
                }).build();
        server.start();

        Env env = new Env(new HashMap<String, String>());
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", env, new TestLogger());
        publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
    }

    @Test
    public void explains_what_to_do_on_auth_error() throws InterruptedException {
        server = Undertow.builder()
                .addHttpListener(8082, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.setStatusCode(401).getResponseSender().close();
                    }
                }).build();
        server.start();

        Env env = new Env(new HashMap<String, String>());
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", env, new TestLogger());
        try {
            publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
            fail();
        } catch (RuntimeException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You need to define the CUCUMBER_PRO_TOKEN environment variable", suggestion);
        }
    }

    @Test
    public void throws_error_with_explanation_on_connection_timeout() throws InterruptedException, IOException {
        Env env = new Env(new HashMap<String, String>() {{
            put(CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS, "100");
        }});
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", env, new TestLogger());
        try {
            publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
            fail();
        } catch (RuntimeException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You can define CUCUMBER_PRO_IGNORE_CONNECTION_ERROR=true to treat this as a warning instead of an error", suggestion);
        }
    }

    @Test
    public void prints_error_on_connection_timeout() throws InterruptedException, IOException {
        Env env = new Env(new HashMap<String, String>() {{
            put(CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, "true");
            put(CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS, "100");
        }});
        TestLogger logger = new TestLogger();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", env, logger);
        publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
        assertEquals("Failed to publish results to http://localhost:8082/results\n", logger.warn.get(0));
    }
}