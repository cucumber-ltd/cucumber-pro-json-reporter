package io.cucumber.pro.publisher;

import cucumber.runtime.CucumberException;

import java.io.File;

class NullPublisher implements Publisher {
    private final String warningMessage;

    NullPublisher(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    @Override
    public void publish(File resultsJsonFile, String env, String profileName) throws CucumberException {
        if (warningMessage != null) {
            System.err.println(warningMessage);
        }
    }
}
