package io.cucumber.pro.documentation;

import io.cucumber.pro.Keys;
import io.cucumber.pro.Logger;
import io.cucumber.pro.environment.CI;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.ProjectName;

public class DocumentationPublisherFactory {

    public static DocumentationPublisher create(Config config, Logger logger) {
        if (!new CI(config).isRunningInCi()) return new NullDocumentationPublisher();

        if (config.getBoolean(Keys.CUCUMBER_PRO_GIT_PUBLISH)) {
            String projectName = new ProjectName(config).getProjectName();
            GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                    CucumberProGitRemoteBuilder.buildCucumberProUrl(config, projectName),
                    config.getInteger(Keys.CUCUMBER_PRO_GIT_SSH_PORT),
                    config.getString(Keys.CUCUMBER_PRO_GIT_HOST_KEY)
            );
            return new GitDocumentationPublisher(pushSpec, config, logger);
        } else {
            return new NullDocumentationPublisher();
        }
    }
}
