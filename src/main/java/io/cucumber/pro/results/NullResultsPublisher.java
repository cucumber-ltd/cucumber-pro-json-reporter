package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;

import java.io.File;
import java.util.Map;

class NullResultsPublisher implements ResultsPublisher {
    NullResultsPublisher() {
    }

    @Override
    public void publish(File resultsJsonFile, Map<String, String> env, String profileName, String revision, String branch, String tag) throws CucumberException {
    }
}
