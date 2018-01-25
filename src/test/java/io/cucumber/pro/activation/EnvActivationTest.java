package io.cucumber.pro.activation;

import io.cucumber.pro.Keys;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CI;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnvActivationTest {
    @Test
    public void is_inactive_by_default() {
        Config config = createConfig();
        assertFalse(new CI(config).isRunningInCi());
    }

    @Test
    public void is_activated_with_CUCUMBER_PRO_PUBLISH() {
        Config config = createConfig();
        config.set(Keys.CUCUMBER_PRO_PUBLISH, "true");
        assertTrue(new CI(config).isRunningInCi());
    }
}
