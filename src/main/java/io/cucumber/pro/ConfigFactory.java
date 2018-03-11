package io.cucumber.pro;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.EnvironmentVariablesConfigLoader;
import io.cucumber.pro.config.loaders.SystemPropertiesConfigLoader;
import io.cucumber.pro.config.loaders.YamlConfigLoader;
import io.cucumber.pro.environment.BambooEnvironmentVariables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

import static io.cucumber.pro.Keys.createConfig;

public class ConfigFactory {
    public static final String[] CONFIG_FILE_NAMES = new String[]{
            "/usr/local/etc/cucumber/cucumber.yml",
            System.getProperty("user.home") + "/cucumber.yml",
            System.getProperty("user.home") + "/.cucumber/cucumber.yml",
            "cucumber.yml",
            ".cucumber/cucumber.yml",
    };

    public static Config create() {
        Config config = createConfig();

        // The order is defined by "globalness". The principle is to make it easy
        // to define global values, but equally easy to override them on a per-project
        // basis.
        load(CONFIG_FILE_NAMES, config);
        new DeprecatedEnvironmentVariablesConfigLoader().load(config);
        new SystemPropertiesConfigLoader().load(config);
        Map<String, String> env = new BambooEnvironmentVariables().convert(System.getenv());
        new EnvironmentVariablesConfigLoader(env).load(config);

        return config;
    }

    public static void load(String[] yamlFileNames, Config config) {
        for (String yamlFileName : yamlFileNames) {
            File file = new File(yamlFileName);
            if (file.isFile()) {
                try {
                    Reader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
                    new YamlConfigLoader(reader).load(config);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
