package io.cucumber.pro.publisher;

import cucumber.runtime.CucumberException;

import java.io.File;

public class NullPublisher implements Publisher {
    private final String message;

    public NullPublisher(String message) {
        this.message = message;
    }

    @Override
    public void publish(File resultsJsonFile, String env, String profileName) throws CucumberException {
        System.err.println(message);
    }
}
