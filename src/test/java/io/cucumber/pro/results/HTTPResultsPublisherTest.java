package io.cucumber.pro.results;

import gherkin.deps.com.google.gson.Gson;
import io.cucumber.pro.Logger;
import io.cucumber.pro.TestLogger;
import io.cucumber.pro.config.Config;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cucumber.pro.Keys.CUCUMBERPRO_CONNECTION_TIMEOUT;
import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;

public class HTTPResultsPublisherTest {

    private static final File RESULTS_JSON_FILE = new File("src/test/resources/sample.json");
    private Undertow server;

    @After
    public void stopServer() {
        if (server != null) server.stop();
    }

    @Test
    public void posts_results_as_json() {
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
                        InputStream inputStream = exchange.getInputStream();
                        ResultSet resultSet = new Gson().fromJson(new InputStreamReader(inputStream, "UTF-8"), ResultSet.class);
                        assertEquals("some-value", resultSet.environment.get("some-env"));
                        assertEquals(1, resultSet.cucumberJson.size());
                        assertEquals("the-rev", resultSet.git.get("revision"));
                        assertEquals("the-branch", resultSet.git.get("branch"));
                        exchange.getResponseSender().send("OK");
                    }
                }).build();
        server.start();

        Config config = createConfig();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, new TestLogger());
        Map<String, String> env = new HashMap<>();
        env.put("some-env", "some-value");
        publisher.publish(RESULTS_JSON_FILE, env, "the-profile", "the-rev", "the-branch", "the-tag");
    }

    @Test
    public void explains_what_to_do_on_auth_error() {
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
        TestLogger logger = new TestLogger();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, logger);
        publisher.publish(RESULTS_JSON_FILE, new HashMap<String, String>(), "the-profile", "the-rev", "the-branch", "the-tag");

        String firstLogMessage = logger.getMessages(Logger.Level.ERROR).get(0);
        assertEquals("Failed to publish results to : http://localhost:8082/results\n" +
                "HTTP Status: HTTP/1.1 401 Unauthorized\n" +
                "\n" +
                "You need to define cucumberpro.token", firstLogMessage);
    }

    @Test
    public void prints_error_on_connection_timeout() {
        Config config = createConfig();
        config.set(CUCUMBERPRO_CONNECTION_TIMEOUT, "100");
        TestLogger logger = new TestLogger();
        HTTPResultsPublisher publisher = new HTTPResultsPublisher("http://localhost:8082/results", config, logger);
        publisher.publish(RESULTS_JSON_FILE, new HashMap<String, String>(), "the-profile", "the-rev", "the-branch", "the-tag");
        String firstLogMessage = logger.getMessages(Logger.Level.ERROR).get(0);
        String firstLine = firstLogMessage.split("\\n")[0];
        assertEquals("Failed to publish results to http://localhost:8082/results", firstLine);
    }

    public static class ResultSet {
        public Map<String, String> environment;
        // It's a more complex structure, but this simple structure is easier to use in a test
        public List<Object> cucumberJson;
        public Map<String, String> git;
    }
}