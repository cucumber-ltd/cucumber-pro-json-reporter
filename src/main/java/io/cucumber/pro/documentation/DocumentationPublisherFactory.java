package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.MetadataFactory;

public class DocumentationPublisherFactory {

    public static final String ENV_CUCUMBER_PRO_SSH_PASSPHRASE = "CUCUMBER_PRO_SSH_PASSPHRASE";

    public static DocumentationPublisher create(Env env) {
        boolean isActive = EnvActivation.create().isActive();
        if (!isActive) return new NullDocumentationPublisher();

        String projectName = MetadataFactory.create(env).getProjectName();
        String remote = CucumberProGitRemoteBuilder.buildCucumberProUrl(env, projectName);
        String passphrase = System.getenv(ENV_CUCUMBER_PRO_SSH_PASSPHRASE);
        return new GitDocumentationPublisher(remote, passphrase);
    }
}
