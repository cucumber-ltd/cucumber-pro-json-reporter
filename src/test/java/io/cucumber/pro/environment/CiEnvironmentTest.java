package io.cucumber.pro.environment;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class CiEnvironmentTest {
    @Test
    public void is_inactive_by_default() {
        Map<String, String> env = new HashMap<>();
        assertNull(CIEnvironment.detect(env));
    }

    @Test
    public void recognises_bamboo() {
        Map<String, String> env = new HashMap<>();
        env.put("bamboo_planRepository_revision", "1234");
        env.put("bamboo_repository_git_branch", "master");
        env.put("bamboo_planRepository_name", "the-project");
        assertEquals("Bamboo", CIEnvironment.detect(env).getCiName());
        assertEquals("1234", CIEnvironment.detect(env).getRevision());
        assertEquals("master", CIEnvironment.detect(env).getBranch());
        assertEquals(null, CIEnvironment.detect(env).getTag());
        assertEquals("the-project", CIEnvironment.detect(env).getProjectName());
    }

    @Test
    public void recognises_circle() {
        Map<String, String> env = new HashMap<>();
        env.put("CIRCLE_SHA1", "1234");
        env.put("CIRCLE_BRANCH", "master");
        env.put("CIRCLE_TAG", "v1");
        env.put("CIRCLE_PROJECT_REPONAME", "the-project");
        assertEquals("Circle CI", CIEnvironment.detect(env).getCiName());
        assertEquals("1234", CIEnvironment.detect(env).getRevision());
        assertEquals("master", CIEnvironment.detect(env).getBranch());
        assertEquals("v1", CIEnvironment.detect(env).getTag());
        assertEquals("the-project", CIEnvironment.detect(env).getProjectName());
    }

    @Test
    public void recognises_jenkins() {
        Map<String, String> env = new HashMap<>();
        env.put("GIT_COMMIT", "1234");
        env.put("GIT_BRANCH", "master");
        env.put("GIT_TAG_NAME", "v1");
        assertEquals("Jenkins", CIEnvironment.detect(env).getCiName());
        assertEquals("1234", CIEnvironment.detect(env).getRevision());
        assertEquals("master", CIEnvironment.detect(env).getBranch());
        assertEquals("v1", CIEnvironment.detect(env).getTag());
        assertEquals(null, CIEnvironment.detect(env).getProjectName());
    }

    @Test
    public void recognises_tfs() {
        Map<String, String> env = new HashMap<>();
        env.put("BUILD_SOURCEVERSION", "1234");
        env.put("BUILD_SOURCEBRANCHNAME", "master");
        env.put("SYSTEM_TEAMPROJECT", "the-project");
        assertEquals("TFS", CIEnvironment.detect(env).getCiName());
        assertEquals("1234", CIEnvironment.detect(env).getRevision());
        assertEquals("master", CIEnvironment.detect(env).getBranch());
        assertEquals(null, CIEnvironment.detect(env).getTag());
        assertEquals("the-project", CIEnvironment.detect(env).getProjectName());
    }

    @Test
    public void recognises_travis() {
        Map<String, String> env = new HashMap<>();
        env.put("TRAVIS_COMMIT", "1234");
        env.put("TRAVIS_BRANCH", "master");
        env.put("TRAVIS_REPO_SLUG", "the-owner/the-project");
        assertEquals("Travis CI", CIEnvironment.detect(env).getCiName());
        assertEquals("1234", CIEnvironment.detect(env).getRevision());
        assertEquals("master", CIEnvironment.detect(env).getBranch());
        assertEquals(null, CIEnvironment.detect(env).getTag());
        assertEquals("the-project", CIEnvironment.detect(env).getProjectName());
    }

    @Test
    public void recognises_wercker() {
        Map<String, String> env = new HashMap<>();
        env.put("WERCKER_GIT_COMMIT", "1234");
        env.put("WERCKER_GIT_BRANCH", "master");
        env.put("WERCKER_GIT_REPOSITORY", "the-project");
        assertEquals("Wercker", CIEnvironment.detect(env).getCiName());
        assertEquals("1234", CIEnvironment.detect(env).getRevision());
        assertEquals("master", CIEnvironment.detect(env).getBranch());
        assertEquals(null, CIEnvironment.detect(env).getTag());
        assertEquals("the-project", CIEnvironment.detect(env).getProjectName());
    }
}
