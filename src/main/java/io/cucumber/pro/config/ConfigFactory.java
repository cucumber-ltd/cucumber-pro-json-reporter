package io.cucumber.pro.config;

public class ConfigFactory {
    public static Config create(String keyPattern) {
        Config config = new Config();

        // TODO: is this the right order?
        YamlConfigLoader.create().load(config);
        new EnvironmentVariablesConfigLoader(keyPattern).load(config);
        new BambooEnvironmentVariablesConfigLoader().load(config);
        new SystemPropertiesConfigLoader().load(config);

        return config;
    }
}
