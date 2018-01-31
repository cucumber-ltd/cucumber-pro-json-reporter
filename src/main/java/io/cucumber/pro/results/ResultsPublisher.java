package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;

import java.io.File;
import java.util.Map;

public interface ResultsPublisher {
    void publish(File resultsJsonFile, Map<String, String> env, String profileName) throws CucumberException;
}
