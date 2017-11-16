package io.cucumber.pro;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter12:smoke"}, tags = "@smoke")
public class CucumberSmokeTest {
}
