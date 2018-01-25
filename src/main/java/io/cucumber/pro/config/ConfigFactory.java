package io.cucumber.pro.config;

import static io.cucumber.pro.Keys.createConfig;

public class ConfigFactory {
    public static Config create() {
        Config config = createConfig();

        // TODO: is this the right order?
        YamlConfigLoader.create().load(config);
        new EnvironmentVariablesConfigLoader().load(config);
        new BambooEnvironmentVariablesConfigLoader().load(config);
        new SystemPropertiesConfigLoader().load(config);

        return config;
    }
}
