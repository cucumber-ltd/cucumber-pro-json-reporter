package io.cucumber.pro;

import cucumber.api.event.EventHandler;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runner.EventBus;
import cucumber.runner.TimeService;
import cucumber.runner.TimeServiceEventBus;
import cucumber.runtime.CucumberException;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.EnvironmentVariablesConfigLoader;
import io.cucumber.pro.environment.CIEnvironment;
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
            put("TRAVIS_BRANCH", "the-branch");
            put("TRAVIS_COMMIT", "the-commit");
            put("TRAVIS_REPO_SLUG", "owner/repo");
        }};
        Config config = createConfig();
        new EnvironmentVariablesConfigLoader(env).load(config);
        CapturingResultsPublisher resultsPublisher = new CapturingResultsPublisher();
        String profileName = "testing-testing";
        Logger logger = new TestLogger();
        Formatter reporter = new JsonReporter(
                resultsPublisher,
                profileName,
                config,
                logger,
                CIEnvironment.detect(env),
                env);

        TimeService timeService = TimeService.SYSTEM;
        EventBus eventBus = new TimeServiceEventBus(timeService);
        reporter.setEventPublisher(eventBus);
        EventHandler<TestRunFinished> testRunFinishedEventHandler = new EventHandler<TestRunFinished>() {
            @Override
            public void receive(TestRunFinished event) {
            }
        };
        eventBus.registerHandlerFor(TestRunFinished.class, testRunFinishedEventHandler);
        eventBus.send(new TestRunFinished(timeService.time()));

        Map<String, String> publishedEnv = resultsPublisher.getPublishedEnv();

        Map<String, String> expectedEnv = new HashMap<String, String>() {{
            put("FOO", "bar");
            put("TRAVIS_BRANCH", "the-branch");
            put("TRAVIS_COMMIT", "the-commit");
            put("TRAVIS_REPO_SLUG", "owner/repo");
        }};
        assertEquals(expectedEnv, publishedEnv);
        assertNotNull(resultsPublisher.getPublishedFile());
    }

    class CapturingResultsPublisher implements ResultsPublisher {

        private File file;
        private Map<String, String> env;

        @Override
        public void publish(File resultsJsonFile, Map<String, String> env, String profileName, String revision, String branch, String tag) throws CucumberException {
            this.file = resultsJsonFile;
            this.env = env;
        }

        public Map<String, String> getPublishedEnv() {
            return env;
        }

        public File getPublishedFile() {
            return file;
        }
    }

}