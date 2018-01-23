package io.cucumber.pro.activation;

import io.cucumber.pro.Env;
import io.cucumber.pro.config.Config;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnvActivationTest {
    @Test
    public void is_active_when_env_var_anything() {
        Config config = new Config();
        config.set(Env.CUCUMBER_PRO_PUBLISH, "true");
        assertTrue(new EnvActivation(config).isActive());
    }

    @Test
    public void is_inactive_when_env_var_false() {
        Config config = new Config();
        config.set(Env.CUCUMBER_PRO_PUBLISH, "false");
        assertFalse(new EnvActivation(config).isActive());
    }

    @Test
    public void is_inactive_when_env_var_undefined() {
        Config config = new Config();
        assertFalse(new EnvActivation(config).isActive());
    }
}
