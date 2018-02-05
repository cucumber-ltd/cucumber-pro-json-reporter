package io.cucumber.pro;

import gherkin.formatter.JSONFormatter;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CIEnvironment;
import io.cucumber.pro.environment.EnvFilter;
import io.cucumber.pro.results.ResultsPublisher;
import io.cucumber.pro.results.ResultsPublisherFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonReporter12 extends JSONFormatter {

    private static final Config CONFIG = ConfigFactory.create();
    private static final Logger LOGGER = new Logger.SystemLogger(CONFIG);
    private static final CIEnvironment CI_ENVIRONMENT = CIEnvironment.detect(CONFIG);

    private static final File jsonFile;

    static {
        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
            jsonFile.deleteOnExit();
        } catch (IOException e) {
            throw LOGGER.log(e, "Failed to create temp file for Cucumber JSON results");
        }
    }

    private final Config config;
    private final Logger logger;
    private final ResultsPublisher resultsPublisher;
    private final String profileName;
    private final Map<String, String> env;

    JsonReporter12(
            ResultsPublisher resultsPublisher,
            String profileName,
            Config config,
            Logger logger,
            CIEnvironment ciEnvironment,
            Map<String, String> env
    ) throws IOException {
        super(new FileWriter(jsonFile));
        this.resultsPublisher = resultsPublisher;
        this.profileName = profileName;
        this.config = config;
        this.logger = logger;

        this.env = new EnvFilter(config).filter(env);
        if (ciEnvironment != null) {
            this.env.put("cucumber_pro_git_branch", ciEnvironment.getBranch(config));
        }
    }

    JsonReporter12(String profileName) throws IOException {
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

    public JsonReporter12(File fileUsedToGetProfileName) throws IOException {
        this(fileUsedToGetProfileName.getName());
        fileUsedToGetProfileName.delete();
    }

    @Override
    public void close() {
        super.close();
        logger.log(Logger.Level.DEBUG, "Cucumber Pro config:\n\n%s", config.toYaml("cucumberpro"));
        this.resultsPublisher.publish(jsonFile, env, profileName);
    }
}


