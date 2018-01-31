package io.cucumber.pro;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.formatter.PluginFactory;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.documentation.DocumentationPublisher;
import io.cucumber.pro.documentation.DocumentationPublisherFactory;
import io.cucumber.pro.environment.EnvFilter;
import io.cucumber.pro.results.ResultsPublisher;
import io.cucumber.pro.results.ResultsPublisherFactory;
import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.revision.RevisionProviderFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReporter implements Formatter {

    private static final Config CONFIG = ConfigFactory.create();
    private static final Logger LOGGER = new Logger.SystemLogger(CONFIG);
    private static final RevisionProvider REVISION_PROVIDER = RevisionProviderFactory.create(CONFIG, LOGGER);
    private final Formatter jsonFormatter;
    private final File jsonFile;
    private final ResultsPublisher resultsPublisher;
    private final String profileName;
    private final Logger logger;
    private final Map<String, String> env;
    private final DocumentationPublisher documentationPublisher;
    private final Config config;

    JsonReporter(
            DocumentationPublisher documentationPublisher,
            ResultsPublisher resultsPublisher,
            String profileName,
            Config config,
            Logger logger,
            RevisionProvider revisionProvider,
            Map<String, String> env
    ) {
        this.documentationPublisher = documentationPublisher;
        this.resultsPublisher = resultsPublisher;
        this.profileName = profileName;
        this.config = config;
        this.logger = logger;
        this.env = new EnvFilter(config).filter(env);
        this.env.put("cucumber_pro_git_branch", revisionProvider.getBranch());

        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
        } catch (IOException e) {
            throw new CucumberException(e);
        }
        jsonFile.deleteOnExit();
        jsonFormatter = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());
    }

    public JsonReporter(String profileName) {
        this(
                DocumentationPublisherFactory.create(CONFIG, LOGGER),
                ResultsPublisherFactory.create(
                        CONFIG,
                        LOGGER,
                        REVISION_PROVIDER
                ),
                profileName,
                CONFIG,
                LOGGER,
                REVISION_PROVIDER,
                System.getenv()
        );
    }

    public JsonReporter() {
        this("cucumber-jvm-unspecified-profile");
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
                        JsonReporter.this.logger.log(Logger.Level.DEBUG, "Cucumber Pro config:\n\n%s", JsonReporter.this.config.toYaml("cucumberpro"));
                        JsonReporter.this.documentationPublisher.publish();
                        JsonReporter.this.resultsPublisher.publish(jsonFile, env, profileName);
                    }
                });
            }
        }
    }
}

