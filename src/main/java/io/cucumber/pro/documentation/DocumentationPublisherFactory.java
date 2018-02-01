package io.cucumber.pro.documentation;

import io.cucumber.pro.Keys;
import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.ProjectName;

public class DocumentationPublisherFactory {

    public static DocumentationPublisher create(Config config, Logger logger) {
        if (!config.getBoolean(Keys.CUCUMBERPRO_GIT_PUBLISH)) return new NullDocumentationPublisher();

        String projectName = new ProjectName(config, logger).getProjectName();
        GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                CucumberProGitRemoteBuilder.buildCucumberProUrl(config, projectName),
                config.getInteger(Keys.CUCUMBERPRO_GIT_SSHPORT),
                config.getString(Keys.CUCUMBERPRO_GIT_HOSTKEY)
        );
        return new GitDocumentationPublisher(pushSpec, config, logger);
    }
}
