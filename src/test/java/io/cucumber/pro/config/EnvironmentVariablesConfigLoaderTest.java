package io.cucumber.pro.config;

import io.cucumber.pro.config.loaders.ConfigLoader;
import io.cucumber.pro.config.loaders.EnvironmentVariablesConfigLoader;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class EnvironmentVariablesConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new EnvironmentVariablesConfigLoader(new HashMap<String, String>() {{
            put("CUCUMBER_FORMAT", "progress");
        }});
    }
}
