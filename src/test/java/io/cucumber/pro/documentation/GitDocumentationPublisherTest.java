package io.cucumber.pro.documentation;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;
import io.cucumber.pro.TestLogger;
import org.eclipse.jgit.api.Git;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static io.cucumber.pro.Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GitDocumentationPublisherTest {
    @Test
    public void throws_error_with_explanation_on_connection_timeout() throws InterruptedException, IOException {
        Env env = new Env(new HashMap<String, String>() {{
            put(CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, "false");
        }});
        TestLogger logger = new TestLogger();

        GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                "git@0.0.0.0",
                9999,
                null
        );
        GitDocumentationPublisher publisher = new GitDocumentationPublisher(pushSpec, env, logger);
        try {
            publisher.publish();
            fail();
        } catch (CucumberException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You can define CUCUMBER_PRO_IGNORE_CONNECTION_ERROR=true to treat this as a warning instead of an error", suggestion);
        }
    }

    @Test
    public void prints_error_on_connection_timeout() throws InterruptedException, IOException {
        Env env = new Env(new HashMap<String, String>());
        TestLogger logger = new TestLogger();
        GitDocumentationPublisher.RemoteSpec pushSpec = new GitDocumentationPublisher.RemoteSpec(
                "git@0.0.0.0",
                9999,
                null
        );
        GitDocumentationPublisher publisher = new GitDocumentationPublisher(pushSpec, env, logger);
        publisher.publish();
        assertEquals("Failed to publish documentation to git@0.0.0.0\n", logger.getMessages(Logger.Level.WARN).get(0));
    }

    @Test
    public void fetches_without_throwing_an_exception() throws IOException {
        Env env = new Env(new HashMap<String, String>());
        TestLogger logger = new TestLogger();
        Git git = GitDocumentationPublisher.getGit();
        GitDocumentationPublisher publisher = new GitDocumentationPublisher(null, env, logger);
        publisher.fetch(git, "origin");
    }
}
