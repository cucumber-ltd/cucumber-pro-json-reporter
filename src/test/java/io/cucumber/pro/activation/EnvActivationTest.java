package io.cucumber.pro.activation;

import io.cucumber.pro.Env;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnvActivationTest {
    @Test
    public void is_active_when_env_var_anything() {
        assertTrue(new EnvActivation(new Env(new HashMap<String, String>() {{
            put("CUCUMBER_PRO_PUBLISH", "true");
        }})).isActive());
    }

    @Test
    public void is_inactive_when_env_var_false() {
        assertFalse(new EnvActivation(new Env(new HashMap<String, String>() {{
            put("CUCUMBER_PRO_PUBLISH", "false");
        }})).isActive());
    }

    @Test
    public void is_inactive_when_env_var_undefined() {
        assertFalse(new EnvActivation(new Env(new HashMap<String, String>())).isActive());
    }
}
