package io.cucumber.pro.environment;

import io.cucumber.pro.Keys;
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
        config.set(Keys.TRAVIS_JOB_NUMBER, "whatever");
        config.set(Keys.TRAVIS_COMMIT, "whatever");
        config.set(Keys.TRAVIS_BRANCH, "whatever");
        assertEquals(CIEnvironment.TRAVIS, CIEnvironment.detect(config));
    }
}
