package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Keys;
import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CIEnvironment;

public class ResultsPublisherFactory {
    public static ResultsPublisher create(Config config, Logger logger, CIEnvironment ciEnvironment) {
        if (ciEnvironment != null) {
            String projectName = config.getString(Keys.CUCUMBERPRO_PROJECTNAME);
            if (projectName == null) {
                projectName = ciEnvironment.getProjectName(config);
            }
            if (projectName == null) throw new CucumberException(String.format(
                    "You have to define %s when running on %s",
                    Keys.CUCUMBERPRO_PROJECTNAME,
                    ciEnvironment.toString()
            ));
            String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, projectName);
            return new HTTPResultsPublisher(url, config, logger);
        } else {
            return new NullResultsPublisher();
        }
    }
}
