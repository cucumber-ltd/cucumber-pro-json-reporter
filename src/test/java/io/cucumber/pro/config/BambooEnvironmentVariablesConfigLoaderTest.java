package io.cucumber.pro.config;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class BambooEnvironmentVariablesConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new BambooEnvironmentVariablesConfigLoader(new HashMap<String, String>() {{
            put("bamboo_CUCUMBER_FORMAT", "progress");
        }});
    }

    @Test
    public void duplicates_bamboo_prefixed_env_vars_with_prefixless_copy() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("bamboo_FOO", "X");
        }};

        Config config = new Config();
        new BambooEnvironmentVariablesConfigLoader(env).load(config);
        new EnvironmentVariablesConfigLoader(env).load(config);

        assertEquals("X", config.get("bamboo_FOO"));
        assertEquals("X", config.get("FOO"));
    }

    @Test
    public void prefixless_env_var_takes_precendence() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("bamboo_BAR", "X");
            put("BAR", "Y");
        }};

        Config config = new Config();
        new BambooEnvironmentVariablesConfigLoader(env).load(config);
        new EnvironmentVariablesConfigLoader(env).load(config);

        assertEquals("Y", config.get("BAR"));
    }
}
