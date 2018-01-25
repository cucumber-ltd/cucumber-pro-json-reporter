package io.cucumber.pro;

import cucumber.api.event.EventHandler;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runner.EventBus;
import cucumber.runner.TimeService;
import cucumber.runtime.CucumberException;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.EnvironmentVariablesConfigLoader;
import io.cucumber.pro.documentation.NullDocumentationPublisher;
import io.cucumber.pro.results.ResultsPublisher;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonReporterTest {

    @Test
    public void it_publishes_the_json_report_with_filtered_environment_data() {
        Map<String, String> env = new HashMap<String, String>() {{
            put("FOO", "bar");
            put("PASSWORD", "secret");

        }};
        Config config = createConfig();
        new EnvironmentVariablesConfigLoader(env).load(config);
        CapturingResultsPublisher resultsPublisher = new CapturingResultsPublisher();
        String profileName = config.getString(Keys.CUCUMBER_PROFILE_NAME);
        Logger logger = new TestLogger();
        Formatter reporter = new JsonReporter(
                new NullDocumentationPublisher(),
                resultsPublisher,
                profileName,
                config,
                logger,
                env);

        TimeService timeService = TimeService.SYSTEM;
        EventBus eventBus = new EventBus(timeService);
        reporter.setEventPublisher(eventBus);
        EventHandler<TestRunFinished> testRunFinishedEventHandler = new EventHandler<TestRunFinished>() {
            @Override
            public void receive(TestRunFinished event) {
            }
        };
        eventBus.registerHandlerFor(TestRunFinished.class, testRunFinishedEventHandler);
        eventBus.send(new TestRunFinished(timeService.time()));

        assertEquals("FOO=bar\n", resultsPublisher.getPublishedEnv());
        assertNotNull(resultsPublisher.getPublishedFile());
    }

    class CapturingResultsPublisher implements ResultsPublisher {

        private File file;
        private String env;

        @Override
        public void publish(File resultsJsonFile, String env, String profileName) throws CucumberException {
            this.file = resultsJsonFile;
            this.env = env;
        }

        public String getPublishedEnv() {
            return env;
        }

        public File getPublishedFile() {
            return file;
        }
    }

}