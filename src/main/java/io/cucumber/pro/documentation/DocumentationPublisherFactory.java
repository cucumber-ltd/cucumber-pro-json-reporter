package io.cucumber.pro.documentation;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.metadata.MetadataFactory;

public class DocumentationPublisherFactory {

    public static DocumentationPublisher create(Env env, Logger logger) {
        boolean isActive = new EnvActivation(env).isActive();
        if (!isActive) return new NullDocumentationPublisher();

        if (env.getBoolean(Env.CUCUMBER_PRO_GIT_PUBLISH, true)) {
            String projectName = MetadataFactory.create(env).getProjectName();
            if (projectName == null)
                throw new CucumberException("Couldn't detect project name. Can't publish documentation to git.");

            GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                    CucumberProGitRemoteBuilder.buildCucumberProUrl(env, projectName),
                    env.getInt(Env.CUCUMBER_PRO_GIT_SSH_PORT, 22),
                    env.get(Env.CUCUMBER_PRO_GIT_HOST_KEY)
            );
            return new GitDocumentationPublisher(pushSpec, env, logger);
        } else {
            return new NullDocumentationPublisher();
        }
    }
}
