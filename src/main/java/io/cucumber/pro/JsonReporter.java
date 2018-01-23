package io.cucumber.pro;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.formatter.PluginFactory;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.ConfigFactory;
import io.cucumber.pro.documentation.DocumentationPublisher;
import io.cucumber.pro.documentation.DocumentationPublisherFactory;
import io.cucumber.pro.results.ResultsPublisher;
import io.cucumber.pro.results.ResultsPublisherFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReporter implements Formatter {

    static final String DEFAULT_CUCUMBER_PROFILE_NAME = "cucumber-jvm-unspecified-profile";
    private static final Config CONFIG = ConfigFactory.create();
    private static final Logger LOGGER = new Logger.SystemLogger(CONFIG);
    private final Formatter jsonFormatter;
    private final File jsonFile;
    private final FilteredEnv filteredEnv;
    private final ResultsPublisher resultsPublisher;
    private final String profileName;
    private final DocumentationPublisher documentationPublisher;

    JsonReporter(DocumentationPublisher documentationPublisher, ResultsPublisher resultsPublisher, String profileName, Config config, Map<String, String> env) {
        this.documentationPublisher = documentationPublisher;
        this.resultsPublisher = resultsPublisher;
        this.profileName = profileName;
        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
        } catch (IOException e) {
            throw new CucumberException(e);
        }
        jsonFile.deleteOnExit();
        jsonFormatter = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());

        filteredEnv = new FilteredEnv(env, config);
    }

    public JsonReporter(String profileName) {
        this(
                DocumentationPublisherFactory.create(CONFIG, LOGGER),
                ResultsPublisherFactory.create(
                        CONFIG,
                        LOGGER
                ),
                profileName,
                CONFIG,
                System.getenv());
    }

    public JsonReporter() {
        this(CONFIG.get(Env.CUCUMBER_PROFILE_NAME, DEFAULT_CUCUMBER_PROFILE_NAME));
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (jsonFormatter == null) return;
        jsonFormatter.setEventPublisher(new PublisherAdapter(publisher));
    }

    private class PublisherAdapter implements EventPublisher {
        private final EventPublisher publisher;

        PublisherAdapter(EventPublisher publisher) {
            this.publisher = publisher;
        }

        @Override
        public <T extends Event> void registerHandlerFor(Class<T> eventType, EventHandler<T> handler) {
            publisher.registerHandlerFor(eventType, handler);

            if (eventType == TestRunFinished.class) {
                publisher.registerHandlerFor(TestRunFinished.class, new EventHandler<TestRunFinished>() {
                    @Override
                    public void receive(TestRunFinished event) {
                        JsonReporter.this.documentationPublisher.publish();
                        JsonReporter.this.resultsPublisher.publish(jsonFile, filteredEnv.toString(), profileName);
                    }
                });
            }
        }
    }
}

