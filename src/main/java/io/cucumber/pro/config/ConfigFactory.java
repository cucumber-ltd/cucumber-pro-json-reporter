package io.cucumber.pro.config;

public class ConfigFactory {
    public static Config create() {
        Config config = new Config();

        // TODO: is this the right order?
        YamlConfigLoader.create().load(config);
        new EnvironmentVariablesConfigLoader().load(config);
        new BambooEnvironmentVariablesConfigLoader().load(config);
        new SystemPropertiesConfigLoader().load(config);

        return config;
    }
}
