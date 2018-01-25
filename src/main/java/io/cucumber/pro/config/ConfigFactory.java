package io.cucumber.pro.config;

import io.cucumber.pro.config.loaders.BambooEnvironmentVariablesConfigLoader;
import io.cucumber.pro.config.loaders.EnvironmentVariablesConfigLoader;
import io.cucumber.pro.config.loaders.SystemPropertiesConfigLoader;
import io.cucumber.pro.config.loaders.YamlConfigLoader;

import static io.cucumber.pro.Keys.createConfig;

public class ConfigFactory {
    public static final String[] YAML_FILE_NAMES = new String[]{
            "cucumber.yml",
            ".cucumber.yml",
            ".cucumber/cucumber.yml",
            ".cucumberpro.yml"
    };

    public static Config create() {
        Config config = createConfig();

        // TODO: is this the right order?
        YamlConfigLoader.load(YAML_FILE_NAMES, config);
        new EnvironmentVariablesConfigLoader().load(config);
        new BambooEnvironmentVariablesConfigLoader().load(config);
        new SystemPropertiesConfigLoader().load(config);

        return config;
    }
}
