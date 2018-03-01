package io.cucumber.pro.environment;

import io.cucumber.pro.config.Config;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class CiEnvironmentTest {
    @Test
    public void is_inactive_by_default() {
        Config config = createConfig();
        assertNull(CIEnvironment.detect(config));
    }

    @Test
    public void recognises_travis() {
        Config config = createConfig();
        config.set(CIEnvironment.TRAVIS.branchVar, "whatever");
        config.set(CIEnvironment.TRAVIS.revisionVar, "whatever");
        assertEquals(CIEnvironment.TRAVIS, CIEnvironment.detect(config));
    }

    @Test
    public void recognises_bamboo() {
        Config config = createConfig();
        config.set(CIEnvironment.BAMBOO.branchVar, "master");
        config.set(CIEnvironment.BAMBOO.revisionVar, "1234");
        config.set(CIEnvironment.BAMBOO.projectNameVar, "the-project");
        assertEquals(CIEnvironment.BAMBOO, CIEnvironment.detect(config));
        assertEquals("the-project", CIEnvironment.detect(config).getProjectName(config));
    }
}
