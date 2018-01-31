package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Logger;

import java.io.File;
import java.util.Map;

class NullResultsPublisher implements ResultsPublisher {
    private final String warningMessage;
    private final Logger logger;

    NullResultsPublisher(Logger logger, String warningMessage) {
        this.logger = logger;
        this.warningMessage = warningMessage;
    }

    @Override
    public void publish(File resultsJsonFile, Map<String, String> env, String profileName) throws CucumberException {
        if (warningMessage != null) {
            logger.log(Logger.Level.WARN, warningMessage);
        }
    }
}
