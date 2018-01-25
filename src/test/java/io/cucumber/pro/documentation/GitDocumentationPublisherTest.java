package io.cucumber.pro.documentation;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Logger;
import io.cucumber.pro.TestLogger;
import io.cucumber.pro.config.Config;
import org.eclipse.jgit.api.Git;
import org.junit.Test;

import java.io.IOException;

import static io.cucumber.pro.Keys.CUCUMBERPRO_CONNECTION_IGNOREERROR;
import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GitDocumentationPublisherTest {
    @Test
    public void throws_error_with_explanation_on_connection_timeout() throws InterruptedException, IOException {
        Config config = createConfig();
        config.set(CUCUMBERPRO_CONNECTION_IGNOREERROR, "false");
        TestLogger logger = new TestLogger();

        GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                "git@0.0.0.0",
                9999,
                null
        );
        GitDocumentationPublisher publisher = new GitDocumentationPublisher(pushSpec, config, logger);
        try {
            publisher.publish();
            fail();
        } catch (CucumberException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You can set cucumberpro.connection.ignoreerror to true to treat this as a warning instead of an error", suggestion);
        }
    }

    @Test
    public void prints_error_on_connection_timeout() throws InterruptedException, IOException {
        Config config = createConfig();
        TestLogger logger = new TestLogger();
        GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                "git@0.0.0.0",
                9999,
                null
        );
        GitDocumentationPublisher publisher = new GitDocumentationPublisher(pushSpec, config, logger);
        publisher.publish();
        assertEquals("Failed to publish documentation to git@0.0.0.0\n", logger.getMessages(Logger.Level.WARN).get(0));
    }

    @Test
    public void fetches_without_throwing_an_exception() throws IOException {
        Config config = createConfig();
        TestLogger logger = new TestLogger();
        Git git = GitDocumentationPublisher.getGit();
        GitDocumentationPublisher publisher = new GitDocumentationPublisher(null, config, logger);
        publisher.fetch(git, "origin");
    }
}
