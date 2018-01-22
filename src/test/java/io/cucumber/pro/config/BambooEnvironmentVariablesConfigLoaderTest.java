package io.cucumber.pro.config;

import java.util.HashMap;

public class BambooEnvironmentVariablesConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new BambooEnvironmentVariablesConfigLoader(new HashMap<String, String>() {{
            put("bamboo_CUCUMBER_FORMAT", "progress");
        }});
    }
}
