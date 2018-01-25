package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.ConfigLoader;

import java.util.Map;

public class EnvironmentVariablesConfigLoader implements ConfigLoader {
    private final Map<String, String> env;

    public EnvironmentVariablesConfigLoader() {
        this(System.getenv());
    }

    public EnvironmentVariablesConfigLoader(Map<String, String> env) {
        this.env = env;
    }

    @Override
    public void load(Config config) {
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            config.set(key, entry.getValue());
        }
    }
}
