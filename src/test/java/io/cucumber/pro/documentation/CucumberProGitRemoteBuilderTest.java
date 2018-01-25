package io.cucumber.pro.documentation;

import io.cucumber.pro.config.Config;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static junit.framework.TestCase.assertEquals;

public class CucumberProGitRemoteBuilderTest {
    @Test
    public void builds_git_ssh_url() {
        Config config = createConfig();
        String url = CucumberProGitRemoteBuilder.buildCucumberProUrl(config, "hello-world");
        assertEquals("git@git.cucumber.pro:hello-world.git", url);
    }
}
