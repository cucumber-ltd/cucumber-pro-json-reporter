package io.cucumber.pro.results;

import io.cucumber.pro.Logger;
import io.cucumber.pro.environment.CI;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.ProjectName;
import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.revision.RevisionProviderFactory;

public class ResultsPublisherFactory {
    public static ResultsPublisher create(Config config, Logger logger) {
        if (!new CI(config).isRunningInCi()) return new NullResultsPublisher(logger, null);

        String projectName = new ProjectName(config).getProjectName();
        RevisionProvider revisionProvider = RevisionProviderFactory.create(config, logger);
        String revision = revisionProvider.getRevision();
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, projectName, revision);
        return new HTTPResultsPublisher(url, config, logger);
    }
}
