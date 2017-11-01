package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;

import java.io.File;

public interface ResultsPublisher {
    void publish(File resultsJsonFile, String env, String profileName) throws CucumberException;
}
