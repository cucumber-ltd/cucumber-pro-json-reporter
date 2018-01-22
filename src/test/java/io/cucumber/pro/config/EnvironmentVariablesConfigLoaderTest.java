package io.cucumber.pro.config;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class EnvironmentVariablesConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new EnvironmentVariablesConfigLoader("^(?:cucumber\\.)", new HashMap<String, String>() {{
            put("CUCUMBER_FORMAT", "progress");
        }});
    }

    @Test
    public void only_sets_config_for_matching_env_vars() {
        Config config = new Config();
        new EnvironmentVariablesConfigLoader("^(?:cucumber\\.)", new HashMap<String, String>() {{
            put("CUCUMBER_FORMAT", "progress");
            put("SOMETHING_ELSE", "somthing");
            put("_", "oops");
        }}).load(config);

        assertEquals("progress", config.get("cucumber.format"));
        assertEquals(null, config.get("something.else"));
    }
}
