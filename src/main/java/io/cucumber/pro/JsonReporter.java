package io.cucumber.pro;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.runtime.formatter.PluginFactory;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CIEnvironment;
import io.cucumber.pro.environment.EnvFilter;
import io.cucumber.pro.results.ResultsPublisher;
import io.cucumber.pro.results.ResultsPublisherFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReporter implements EventListener {

    private static final Config CONFIG = ConfigFactory.create();
    private static final Logger LOGGER = new Logger.SystemLogger(CONFIG);
    private static final CIEnvironment CI_ENVIRONMENT = CIEnvironment.detect(System.getenv());
    private final EventListener jsonFormatter;
    private final File jsonFile;
    private final ResultsPublisher resultsPublisher;
    private final String profileName;
    private final Logger logger;
    private final CIEnvironment ciEnvironment;
    private final Map<String, String> env;
    private final Config config;

    JsonReporter(
            ResultsPublisher resultsPublisher,
            String profileName,
            Config config,
            Logger logger,
            CIEnvironment ciEnvironment,
            Map<String, String> env
    ) {
        this.resultsPublisher = resultsPublisher;
        this.profileName = ProfileName.getProfileName(config, profileName);
        this.config = config;
        this.logger = logger;
        this.ciEnvironment = ciEnvironment;
        this.env = new EnvFilter(config).filter(env);

        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
        } catch (IOException e) {
            throw logger.log(e, "Failed to create temp file for Cucumber JSON results");
        }
        jsonFormatter = (EventListener) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());
    }

    public JsonReporter(String profileName) {
        this(
                ResultsPublisherFactory.create(
                        CONFIG,
                        LOGGER,
                        CI_ENVIRONMENT
                ),
                profileName,
                CONFIG,
                LOGGER,
                CI_ENVIRONMENT,
                System.getenv()
        );
    }

    public JsonReporter() {
        this(null);
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (this.ciEnvironment != null) {
            String revision = ciEnvironment.getRevision();
            String branch = ciEnvironment.getBranch();
            String tag = ciEnvironment.getTag();
            jsonFormatter.setEventPublisher(new PublisherAdapter(publisher, revision, branch, tag));
        }
    }

    private class PublisherAdapter implements EventPublisher {
        private final EventPublisher publisher;
        private final EventHandler<TestRunFinished> publisherHandler;

        PublisherAdapter(EventPublisher publisher, final String revision, final String branch, final String tag) {
            this.publisher = publisher;
            this.publisherHandler = new EventHandler<TestRunFinished>() {
                @Override
                public void receive(TestRunFinished event) {
                    JsonReporter.this.logger.log(Logger.Level.DEBUG, "Cucumber Pro config:\n\n%s", JsonReporter.this.config.toYaml("cucumberpro"));
                    JsonReporter.this.resultsPublisher.publish(jsonFile, env, profileName, revision, branch, tag);
                    jsonFile.deleteOnExit(); // Defer deleting file until successful publication
                }
            };
        }

        @Override
        public <T extends Event> void registerHandlerFor(Class<T> eventType, EventHandler<T> handler) {
            publisher.registerHandlerFor(eventType, handler);

            if (eventType == TestRunFinished.class) {
                publisher.registerHandlerFor(TestRunFinished.class, publisherHandler);
            }
        }

        @Override
        public <T extends Event> void removeHandlerFor(Class<T> eventType, EventHandler<T> handler) {
            if (eventType == TestRunFinished.class) {
                publisher.removeHandlerFor(TestRunFinished.class, publisherHandler);
            }

            publisher.removeHandlerFor(eventType, handler);
        }
    }
}

