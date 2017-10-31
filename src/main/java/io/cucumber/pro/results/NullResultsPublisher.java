package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;

import java.io.File;

class NullResultsPublisher implements ResultsPublisher {
    private final String warningMessage;

    NullResultsPublisher(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    @Override
    public void publish(File resultsJsonFile, String env, String profileName) throws CucumberException {
        if (warningMessage != null) {
            System.err.println(warningMessage);
        }
    }
}
