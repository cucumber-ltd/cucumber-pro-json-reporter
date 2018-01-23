package io.cucumber.pro.config;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public abstract class ConfigLoaderContract {
    @Test
    public void creates_map_with_env() {
        Config config = new Config();
        ConfigLoader configLoader = makeConfigLoader();
        configLoader.load(config);

        assertEquals("progress", config.get("cucumber.format"));
        assertEquals("progress", config.get("CUCUMBER_FORMAT"));
    }

    protected abstract ConfigLoader makeConfigLoader();
}
