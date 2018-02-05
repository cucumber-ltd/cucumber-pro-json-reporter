package io.cucumber.pro.results;

import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CIEnvironment;
import io.cucumber.pro.environment.ProjectName;

public class ResultsPublisherFactory {
    public static ResultsPublisher create(Config config, Logger logger, CIEnvironment ciEnvironment) {
        if (ciEnvironment != null) {
            String projectName = new ProjectName(config, logger).getProjectName();
            String revision = ciEnvironment.getRevision(config);
            String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, projectName, revision);
            return new HTTPResultsPublisher(url, config, logger);
        } else {
            return new NullResultsPublisher(logger, null);
        }
    }
}
