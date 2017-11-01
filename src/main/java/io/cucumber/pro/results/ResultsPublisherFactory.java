package io.cucumber.pro.results;

import io.cucumber.pro.Env;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.MetadataFactory;
import io.cucumber.pro.revision.RevisionProviderFactory;

import static io.cucumber.pro.metadata.EnvMetadata.ENV_CUCUMBER_PRO_PROJECT_NAME;
import static io.cucumber.pro.metadata.YamlMetadata.PROJECT_NAME_FIELD;
import static io.cucumber.pro.metadata.YamlMetadata.YAML_FILE_NAME;

public class ResultsPublisherFactory {
    public static ResultsPublisher create(Env env) {
        boolean isActive = new EnvActivation(env).isActive();
        if (!isActive) return new NullResultsPublisher(null);

        String projectName = MetadataFactory.create(env).getProjectName();
        if (projectName == null) {
            String message = String.format("Project name missing. Either define an environment variable called %s or create %s with key %s", ENV_CUCUMBER_PRO_PROJECT_NAME, YAML_FILE_NAME, PROJECT_NAME_FIELD);
            return new NullResultsPublisher(message);
        }
        String revision = RevisionProviderFactory.create(env).getRevision();
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(env, projectName, revision);
        return new HTTPResultsPublisher(url, env);
    }
}
