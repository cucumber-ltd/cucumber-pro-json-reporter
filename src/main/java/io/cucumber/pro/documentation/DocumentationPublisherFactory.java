package io.cucumber.pro.documentation;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;
import io.cucumber.pro.activation.EnvActivation;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.metadata.MetadataFactory;

public class DocumentationPublisherFactory {

    public static DocumentationPublisher create(Config config, Logger logger) {
        boolean isActive = new EnvActivation(config).isActive();
        if (!isActive) return new NullDocumentationPublisher();

        if (config.getBoolean(Env.CUCUMBER_PRO_GIT_PUBLISH, false)) {
            String projectName = MetadataFactory.create(config).getProjectName();
            if (projectName == null)
                throw new CucumberException("Couldn't detect project name. Can't publish documentation to git.");

            GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                    CucumberProGitRemoteBuilder.buildCucumberProUrl(config, projectName),
                    config.getInt(Env.CUCUMBER_PRO_GIT_SSH_PORT, 22),
                    config.get(Env.CUCUMBER_PRO_GIT_HOST_KEY)
            );
            return new GitDocumentationPublisher(pushSpec, config, logger);
        } else {
            return new NullDocumentationPublisher();
        }
    }
}
