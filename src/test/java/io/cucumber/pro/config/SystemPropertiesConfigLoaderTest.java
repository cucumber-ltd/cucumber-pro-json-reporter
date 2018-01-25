package io.cucumber.pro.config;

import io.cucumber.pro.config.loaders.ConfigLoader;
import io.cucumber.pro.config.loaders.SystemPropertiesConfigLoader;

import java.util.HashMap;

public class SystemPropertiesConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new SystemPropertiesConfigLoader(new HashMap<Object, Object>() {{
            put("cucumber.format", "progress");
        }});
    }
}
