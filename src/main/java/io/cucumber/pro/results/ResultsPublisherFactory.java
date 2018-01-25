package io.cucumber.pro.results;

import io.cucumber.pro.Keys;
import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CiDetection;
import io.cucumber.pro.environment.ProjectName;
import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.revision.RevisionProviderFactory;

public class ResultsPublisherFactory {
    public static ResultsPublisher create(Config config, Logger logger) {
        boolean explicitPublish = !config.isNull(Keys.CUCUMBERPRO_RESULTS_PUBLISH) && config.getBoolean(Keys.CUCUMBERPRO_RESULTS_PUBLISH);
        if (new CiDetection(config).isRunningInCi() || explicitPublish) {
            String projectName = new ProjectName(config).getProjectName();
            RevisionProvider revisionProvider = RevisionProviderFactory.create(config, logger);
            String revision = revisionProvider.getRevision();
            String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, projectName, revision);
            return new HTTPResultsPublisher(url, config, logger);
        } else {
            return new NullResultsPublisher(logger, null);
        }
    }
}
