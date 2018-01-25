package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Logger;
import io.cucumber.pro.TestLogger;
import io.cucumber.pro.config.Config;
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

import static io.cucumber.pro.Keys.CUCUMBERPRO_CONNECTION_IGNOREERROR;
import static io.cucumber.pro.Keys.CUCUMBERPRO_CONNECTION_TIMEOUT;
import static io.cucumber.pro.Keys.createConfig;
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

        Config config = createConfig();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, new TestLogger());
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

        Config config = createConfig();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, new TestLogger());
        try {
            publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
            fail();
        } catch (CucumberException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You need to define cucumberpro.token", suggestion);
        }
    }

    @Test
    public void throws_error_with_explanation_on_connection_timeout() throws InterruptedException, IOException {
        Config config = createConfig();
        config.set(CUCUMBERPRO_CONNECTION_IGNOREERROR, "false");
        config.set(CUCUMBERPRO_CONNECTION_TIMEOUT, "100");
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, new TestLogger());
        try {
            publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
            fail();
        } catch (CucumberException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You can set cucumberpro.connection.ignoreerror to true to treat this as a warning instead of an error", suggestion);
        }
    }

    @Test
    public void prints_error_on_connection_timeout() throws InterruptedException, IOException {
        Config config = createConfig();
        config.set(CUCUMBERPRO_CONNECTION_TIMEOUT, "100");
        TestLogger logger = new TestLogger();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, logger);
        publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
        assertEquals("Failed to publish results to http://localhost:8082/results\n", logger.getMessages(Logger.Level.WARN).get(0));
    }
}