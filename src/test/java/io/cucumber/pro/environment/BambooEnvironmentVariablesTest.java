package io.cucumber.pro.environment;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BambooEnvironmentVariablesTest {
    @Test
    public void duplicates_bamboo_prefixed_env_vars_with_prefixless_copy() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("bamboo_FOO", "X");
        }};
        Map<String, String> convertedEnv = new BambooEnvironmentVariables().convert(env);

        assertEquals("X", convertedEnv.get("bamboo_FOO"));
        assertEquals("X", convertedEnv.get("FOO"));
    }

    @Test
    public void prefixless_env_var_takes_precendence() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("bamboo_BAR", "X");
            put("BAR", "Y");
        }};

        Map<String, String> convertedEnv = new BambooEnvironmentVariables().convert(env);

        assertEquals("Y", convertedEnv.get("BAR"));
    }
}
