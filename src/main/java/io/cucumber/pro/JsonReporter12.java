package io.cucumber.pro;

import cucumber.runtime.CucumberException;
import gherkin.formatter.JSONFormatter;
import io.cucumber.pro.documentation.DocumentationPublisher;
import io.cucumber.pro.documentation.DocumentationPublisherFactory;
import io.cucumber.pro.results.ResultsPublisher;
import io.cucumber.pro.results.ResultsPublisherFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonReporter12 extends JSONFormatter {

    static final String DEFAULT_ENV_MASK = "SECRET|KEY|TOKEN|PASSWORD";
    static final String DEFAULT_CUCUMBER_PROFILE_NAME = "cucumber-jvm-unspecified-profile";
    private static final Env ENV = new Env(System.getenv());
    private static final File jsonFile;

    static {
        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
            jsonFile.deleteOnExit();
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private final FilteredEnv filteredEnv;
    private final ResultsPublisher resultsPublisher;
    private final String profileName;
    private final DocumentationPublisher documentationPublisher;

    JsonReporter12(DocumentationPublisher documentationPublisher, ResultsPublisher resultsPublisher, Map<String, String> env, String envMask, String profileName) throws IOException {
        super(new FileWriter(jsonFile));
        this.documentationPublisher = documentationPublisher;
        this.resultsPublisher = resultsPublisher;
        this.profileName = profileName;
        filteredEnv = new FilteredEnv(envMask, env);
    }

    public JsonReporter12(String profileName) throws IOException {
        this(DocumentationPublisherFactory.create(ENV), ResultsPublisherFactory.create(ENV, Logger.System), System.getenv(), ENV.get(Env.CUCUMBER_PRO_ENV_MASK, DEFAULT_ENV_MASK), profileName);
    }

    public JsonReporter12() throws IOException {
        this(ENV.get("CUCUMBER_PROFILE_NAME", DEFAULT_CUCUMBER_PROFILE_NAME));
    }

    @Override
    public void close() {
        super.close();
        this.documentationPublisher.publish();
        this.resultsPublisher.publish(jsonFile, filteredEnv.toString(), profileName);
    }
}


