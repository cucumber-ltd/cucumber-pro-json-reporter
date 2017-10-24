package pro.cucumber;

import cucumber.runtime.CucumberException;

import java.io.File;

public interface Publisher {
    void publish(File file, String env, String profileName) throws CucumberException;
}
