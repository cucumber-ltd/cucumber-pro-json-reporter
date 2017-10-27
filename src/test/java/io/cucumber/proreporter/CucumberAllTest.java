package io.cucumber.proreporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.proreporter.JsonReporter:all"})
public class CucumberAllTest {
}
