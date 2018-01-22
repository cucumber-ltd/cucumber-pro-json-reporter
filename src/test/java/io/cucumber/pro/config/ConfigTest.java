package io.cucumber.pro.config;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ConfigTest {
    @Test
    public void gets_and_sets_value() {
        Config config = new Config();
        config.setValue("name", "progress");
        assertEquals("progress", config.get("name"));
    }

    @Test
    public void gets_deep_value() {
        Config config = new Config();
        Config cucumber = new Config();
        config.setConfig("cucumber", cucumber);
        cucumber.setValue("format", "progress");
        assertEquals("progress", config.get("cucumber.format"));
    }
}
