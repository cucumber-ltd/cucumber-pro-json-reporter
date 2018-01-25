package io.cucumber.pro;

import cucumber.runtime.CucumberException;
import gherkin.formatter.JSONFormatter;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.ConfigFactory;
import io.cucumber.pro.documentation.DocumentationPublisher;
import io.cucumber.pro.documentation.DocumentationPublisherFactory;
import io.cucumber.pro.results.ResultsPublisher;
import io.cucumber.pro.results.ResultsPublisherFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonReporter12 extends JSONFormatter {

    private static final Config CONFIG = ConfigFactory.create();
    private static final Logger LOGGER = new Logger.SystemLogger(CONFIG);
    private static final File jsonFile;

    static {
        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
            jsonFile.deleteOnExit();
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private final Config config;
    private final Logger logger;
    private final FilteredEnv filteredEnv;
    private final ResultsPublisher resultsPublisher;
    private final String profileName;
    private final DocumentationPublisher documentationPublisher;

    JsonReporter12(DocumentationPublisher documentationPublisher, ResultsPublisher resultsPublisher, String profileName, Config config, Logger logger, Map<String, String> env) throws IOException {
        super(new FileWriter(jsonFile));
        this.documentationPublisher = documentationPublisher;
        this.resultsPublisher = resultsPublisher;
        this.profileName = profileName;
        this.config = config;
        this.logger = logger;
        this.filteredEnv = new FilteredEnv(env, CONFIG);
    }

    JsonReporter12(String profileName) throws IOException {
        this(
                DocumentationPublisherFactory.create(CONFIG, LOGGER),
                ResultsPublisherFactory.create(
                        CONFIG,
                        LOGGER
                ),
                profileName,
                CONFIG,
                LOGGER,
                System.getenv()
        );
    }

    public JsonReporter12(File fileUsedToGetProfileName) throws IOException {
        this(fileUsedToGetProfileName.getName());
        fileUsedToGetProfileName.delete();
    }

    @Override
    public void close() {
        super.close();
        logger.log(Logger.Level.DEBUG, "Cucumber Pro config:\n\n%s", config.toYaml("cucumberpro"));
        this.documentationPublisher.publish();
        this.resultsPublisher.publish(jsonFile, filteredEnv.toString(), profileName);
    }
}


