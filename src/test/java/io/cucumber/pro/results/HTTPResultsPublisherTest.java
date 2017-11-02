package io.cucumber.pro.results;

import io.cucumber.pro.Env;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HTTPResultsPublisherTest {

    private Undertow server;

    @After
    public void stopServer() {
        server.stop();
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
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", env);
        publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
    }

    @Test
    public void explain_what_to_do_on_auth_error() throws InterruptedException {
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
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", env);
        try {
            publisher.publish(new File("README.md"), "FOO=BAR", "the-profile");
            fail();
        } catch (RuntimeException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You need to define the CUCUMBER_PRO_TOKEN environment variable", suggestion);
        }
    }
}