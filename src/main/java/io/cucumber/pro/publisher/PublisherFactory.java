package io.cucumber.pro.publisher;

import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.ChainedMetadata;
import io.cucumber.pro.revision.RevisionProviderFactory;

import java.util.Map;

import static io.cucumber.pro.metadata.EnvMetadata.ENV_CUCUMBER_PRO_PROJECT_NAME;
import static io.cucumber.pro.metadata.YamlMetadata.PROJECT_NAME_FIELD;
import static io.cucumber.pro.metadata.YamlMetadata.YAML_FILE_NAME;

public class PublisherFactory {
    public static Publisher create() {
        String projectName = ChainedMetadata.create().getProjectName();
        String revision = RevisionProviderFactory.create().getRevision();
        boolean isActive = EnvActivation.create().isActive();
        Map<String, String> env = System.getenv();
        if (!isActive) return new NullPublisher(null);
        if (projectName == null) {
            String message = String.format("Project name missing. Either define an environment variable called %s or create %s with key %s", ENV_CUCUMBER_PRO_PROJECT_NAME, YAML_FILE_NAME, PROJECT_NAME_FIELD);
            return new NullPublisher(message);
        }
        return new HTTPPublisher(CucumberProUrlBuilder.buildCucumberProUrl(env, projectName, revision), env);
    }
}
