package pro.cucumber;

import cucumber.runtime.CucumberException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface Publisher {
    void publish(File file, String env) throws CucumberException;
}
