package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

public class CucumberProGitRemoteBuilderTest {
    @Test
    public void builds_git_ssh_url() {
        Env env = new Env(new HashMap<String, String>());
        String url = CucumberProGitRemoteBuilder.buildCucumberProUrl(env, "hello-world");
        assertEquals("git@git.cucumber.pro:hello-world.git", url);
    }
}
