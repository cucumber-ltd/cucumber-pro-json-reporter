package io.cucumber.pro.activation;

import io.cucumber.pro.Keys;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CiDetection;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CiDetectionTest {
    @Test
    public void is_inactive_by_default() {
        Config config = createConfig();
        assertFalse(new CiDetection(config).isRunningInCi());
    }

    @Test
    public void is_activated_with_TRAVIS_JOB_NUMBER() {
        Config config = createConfig();
        config.set(Keys.TRAVIS_JOB_NUMBER, "88");
        assertTrue(new CiDetection(config).isRunningInCi());
    }
}
