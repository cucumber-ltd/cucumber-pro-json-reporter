package io.cucumber.pro.results;

import io.cucumber.pro.Logger;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.metadata.MetadataFactory;
import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.revision.RevisionProviderFactory;

import static io.cucumber.pro.Env.CUCUMBER_PRO_PROJECT_NAME;
import static io.cucumber.pro.metadata.YamlMetadata.PROJECT_NAME_FIELD;
import static io.cucumber.pro.metadata.YamlMetadata.YAML_FILE_NAME;

public class ResultsPublisherFactory {
    public static ResultsPublisher create(Config config, Logger logger) {
        boolean isActive = new EnvActivation(config).isActive();
        if (!isActive) return new NullResultsPublisher(logger, null);

        String projectName = MetadataFactory.create(config).getProjectName();
        if (projectName == null) {
            String message = String.format("Project name missing. Either define an environment variable called %s or create %s with key %s", CUCUMBER_PRO_PROJECT_NAME, YAML_FILE_NAME, PROJECT_NAME_FIELD);
            return new NullResultsPublisher(logger, message);
        }
        RevisionProvider revisionProvider = RevisionProviderFactory.create(config, logger);
        String revision = revisionProvider.getRevision();
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, projectName, revision);
        return new HTTPResultsPublisher(url, config, logger);
    }
}
