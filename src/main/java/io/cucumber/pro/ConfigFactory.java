package io.cucumber.pro;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.EnvironmentVariablesConfigLoader;
import io.cucumber.pro.config.loaders.SystemPropertiesConfigLoader;
import io.cucumber.pro.config.loaders.YamlConfigLoader;
import io.cucumber.pro.environment.BambooEnvironmentVariables;

import java.util.Map;

import static io.cucumber.pro.Keys.createConfig;

public class ConfigFactory {
    public static final String[] GLOBAL_YAML_FILE_NAMES = new String[]{
            "/usr/local/etc/cucumber/cucumber.yml",
            System.getProperty("user.home") + "/.cucumber.yml",
    };

    public static final String[] LOCAL_YAML_FILE_NAMES = new String[]{
            "cucumber.yml",
            ".cucumber.yml",
            ".cucumber/cucumber.yml",
            ".cucumber/cucumber.yml",
            ".cucumberpro.yml"
    };

    public static Config create() {
        Config config = createConfig();

        // The order is defined by "globalness". The principle is to make it easy
        // to define global values, but equally easy to override them on a per-project
        // basis.
        YamlConfigLoader.load(GLOBAL_YAML_FILE_NAMES, config);
        YamlConfigLoader.load(LOCAL_YAML_FILE_NAMES, config);
        new DeprecatedEnvironmentVariablesConfigLoader().load(config);
        new SystemPropertiesConfigLoader().load(config);
        Map<String, String> env = new BambooEnvironmentVariables().convert(System.getenv());
        new EnvironmentVariablesConfigLoader(env).load(config);

        return config;
    }
}
