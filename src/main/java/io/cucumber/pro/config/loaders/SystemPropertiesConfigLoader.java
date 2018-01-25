package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.ConfigLoader;

import java.util.Map;

public class SystemPropertiesConfigLoader implements ConfigLoader {
    private final Map<Object, Object> properties;

    public SystemPropertiesConfigLoader() {
        this(System.getProperties());
    }

    public SystemPropertiesConfigLoader(Map<Object, Object> properties) {
        this.properties = properties;
    }

    @Override
    public void load(Config config) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key instanceof String && value instanceof String) {
                config.set((String) key, (String) value);
            }
        }
    }
}
