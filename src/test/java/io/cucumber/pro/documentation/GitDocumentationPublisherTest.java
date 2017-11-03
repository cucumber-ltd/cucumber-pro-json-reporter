package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.TestLogger;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static io.cucumber.pro.Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GitDocumentationPublisherTest {
    @Test
    public void throws_error_with_explanation_on_connection_timeout() throws InterruptedException, IOException {
        Env env = new Env(new HashMap<String, String>());
        TestLogger logger = new TestLogger();
        GitDocumentationPublisher publisher = new GitDocumentationPublisher("git@badhost", null, env, logger);
        try {
            publisher.publish();
            fail();
        } catch (RuntimeException expected) {
            String[] lines = expected.getMessage().split("\\n");
            String suggestion = lines[lines.length - 1];
            assertEquals("You can define CUCUMBER_PRO_IGNORE_CONNECTION_ERROR=true to treat this as a warning instead of an error", suggestion);
        }
    }

    @Test
    public void prints_error_on_connection_timeout() throws InterruptedException, IOException {
        Env env = new Env(new HashMap<String, String>() {{
            put(CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, "true");
        }});
        TestLogger logger = new TestLogger();
        GitDocumentationPublisher publisher = new GitDocumentationPublisher("git@badhost", null, env, logger);
        publisher.publish();
        assertEquals("Failed to publish documentation to git@badhost\n", logger.warn.get(0));
    }
}
