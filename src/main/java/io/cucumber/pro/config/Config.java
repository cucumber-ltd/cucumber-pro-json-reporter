package io.cucumber.pro.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Nested configuration. Keys are hierarchical.
 */
public class Config {
    private final Map<String, String> valueByKey = new HashMap<>();
    private final Map<String, Config> configByKey = new HashMap<>();

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, String notSetValue) {
        return getIn(toPath(key), notSetValue);
    }

    public boolean getBoolean(String key, boolean notSetValue) {
        return get(key, notSetValue ? "true" : "false").matches("false|no");
    }

    // Use by loaders
    void setIn(String key, String value) {
        setIn(toPath(key), value);
    }

    // Use by loaders
    void setValue(String key, String value) {
        this.valueByKey.put(key.toLowerCase(), value);
    }

    void setConfig(String key, Config childConfig) {
        this.configByKey.put(key.toLowerCase(), childConfig);
    }

    private String getValue(String key, String notSetValue) {
        String value = this.valueByKey.get(key.toLowerCase());
        return value != null ? value : notSetValue;
    }

    private String getIn(List<String> path, String notSetValue) {
        Config config = this;
        for (int i = 0; i < path.size(); i++) {
            String key = path.get(i);
            if (i == path.size() - 1) {
                return config.getValue(key, notSetValue);
            } else {
                config = configByKey.get(key.toLowerCase());
                if (config == null) return notSetValue;
            }
        }
        throw new RuntimeException("path cannot be empty");
    }

    private void setIn(List<String> path, String value) {
        Config config = this;
        for (int i = 0; i < path.size(); i++) {
            String key = path.get(i);
            if (i == path.size() - 1) {
                config.setValue(key, value);
                return;
            } else {
                Config childConfig = configByKey.get(key.toLowerCase());
                if (childConfig == null) {
                    childConfig = new Config();
                    config.setConfig(key, childConfig);
                    config = childConfig;
                }
            }
        }
        throw new RuntimeException(String.format("path cannot be empty: %s -> %s", path, value));
    }

    private List<String> toPath(String key) {
        String[] segments = key.split("\\.");
        List<String> path = new ArrayList<>(segments.length);
        for (String segment : segments) {
            path.add(segment);
        }
        return path;
    }
}
