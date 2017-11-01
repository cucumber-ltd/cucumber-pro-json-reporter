package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.MetadataFactory;

public class DocumentationPublisherFactory {

    private static final String ENV_CUCUMBER_PRO_GIT_PUBLISH = "CUCUMBER_PRO_GIT_PUBLISH";

    public static DocumentationPublisher create(Env env) {
        boolean isActive = new EnvActivation(env).isActive();
        if (!isActive) return new NullDocumentationPublisher();

        if (env.getBoolean(ENV_CUCUMBER_PRO_GIT_PUBLISH, true)) {
            String projectName = MetadataFactory.create(env).getProjectName();
            String remote = CucumberProGitRemoteBuilder.buildCucumberProUrl(env, projectName);
            return new GitDocumentationPublisher(remote, env);
        } else {
            return new NullDocumentationPublisher();
        }
    }
}
