package io.cucumber.pro;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnvTest {

    public static final String SOME_ENV_VAR = "SOME_ENV_VAR";

    @Test
    public void is_active_when_env_var_anything() {
        assertTrue(new Env(new HashMap<String, String>() {{
            put(SOME_ENV_VAR, "true");
        }}).getBoolean(SOME_ENV_VAR, false));
    }

    @Test
    public void is_inactive_when_env_var_false() {
        assertFalse(new Env(new HashMap<String, String>() {{
            put(SOME_ENV_VAR, "false");
        }}).getBoolean(SOME_ENV_VAR, false));
    }

    @Test
    public void is_inactive_when_env_var_undefined() {
        assertFalse(new Env(new HashMap<String, String>()).getBoolean(SOME_ENV_VAR, false));
    }
}
