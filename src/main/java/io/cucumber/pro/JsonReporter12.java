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

public class JsonReporter12 extends JSONFormatter {

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

    JsonReporter12(DocumentationPublisher documentationPublisher, ResultsPublisher resultsPublisher, Env env, String profileName) throws IOException {
        super(new FileWriter(jsonFile));
        this.documentationPublisher = documentationPublisher;
        this.resultsPublisher = resultsPublisher;
        this.profileName = profileName;
        filteredEnv = new FilteredEnv(env);
    }

    JsonReporter12(String profileName) throws IOException {
        this(
                DocumentationPublisherFactory.create(ENV),
                ResultsPublisherFactory.create(
                        ENV,
                        Logger.System
                ),
                ENV,
                profileName
        );
    }

    public JsonReporter12(File fileUsedToGetProfileName) throws IOException {
        this(fileUsedToGetProfileName.getName());
        fileUsedToGetProfileName.delete();
    }

    @Override
    public void close() {
        super.close();
        this.documentationPublisher.publish();
        this.resultsPublisher.publish(jsonFile, filteredEnv.toString(), profileName);
    }
}

