package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.MetadataFactory;

public class DocumentationPublisherFactory {

    public static DocumentationPublisher create(Env env) {
        boolean isActive = new EnvActivation(env).isActive();
        if (!isActive) return new NullDocumentationPublisher();

        if (env.getBoolean(Env.CUCUMBER_PRO_GIT_PUBLISH, true)) {
            String projectName = MetadataFactory.create(env).getProjectName();
            if (projectName == null)
                throw new RuntimeException("Couldn't detect project name. Can't publish documentation to git.");
            String remote = CucumberProGitRemoteBuilder.buildCucumberProUrl(env, projectName);
            String hostKey = env.get(Env.CUCUMBER_PRO_GIT_HOST_KEY);
            return new GitDocumentationPublisher(remote, hostKey, env, Logger.System);
        } else {
            return new NullDocumentationPublisher();
        }
    }
}
