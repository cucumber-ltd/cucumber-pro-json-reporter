package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.MetadataFactory;

public class DocumentationPublisherFactory {

    public static final String ENV_CUCUMBER_PRO_SSH_PASSPHRASE = "CUCUMBER_PRO_SSH_PASSPHRASE";
    public static final String ENV_CUCUMBER_PRO_GIT_PUBLISH = "CUCUMBER_PRO_GIT_PUBLISH";

    public static DocumentationPublisher create(Env env) {
        boolean isActive = new EnvActivation(env).isActive();
        if (!isActive) return new NullDocumentationPublisher();

        if (env.getBoolean(ENV_CUCUMBER_PRO_GIT_PUBLISH, false)) {
            String projectName = MetadataFactory.create(env).getProjectName();
            String remote = CucumberProGitRemoteBuilder.buildCucumberProUrl(env, projectName);
            String passphrase = env.get(ENV_CUCUMBER_PRO_SSH_PASSPHRASE, null);
            return new GitDocumentationPublisher(remote, passphrase);
        } else {
            return new NullDocumentationPublisher();
        }
    }
}
