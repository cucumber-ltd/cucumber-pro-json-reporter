package io.cucumber.pro.config;

import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class YamlConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new YamlConfigLoader(new StringReader("" +
                "cucumber:\n" +
                "  format: progress\n"));
    }

    @Test
    public void removes_underscores_from_keys() {
        Config config = new Config();
        ConfigLoader configLoader = new YamlConfigLoader(new StringReader("" +
                "cucumber:\n" +
                "  f_or_mat_: progress\n"));
        configLoader.load(config);

        assertEquals("progress", config.getString("cucumber.format"));
    }
}
