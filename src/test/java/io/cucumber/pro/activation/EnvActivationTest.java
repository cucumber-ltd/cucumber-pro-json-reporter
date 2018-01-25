package io.cucumber.pro.activation;

import io.cucumber.pro.Env;
import io.cucumber.pro.config.Config;
import org.junit.Test;

import static io.cucumber.pro.Env.createConfig;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnvActivationTest {
    @Test
    public void is_inactive_by_default() {
        Config config = createConfig();
        assertFalse(new EnvActivation(config).isActive());
    }

    @Test
    public void is_activated_with_CUCUMBER_PRO_PUBLISH() {
        Config config = createConfig();
        config.set(Env.CUCUMBER_PRO_PUBLISH, "true");
        assertTrue(new EnvActivation(config).isActive());
    }
}
