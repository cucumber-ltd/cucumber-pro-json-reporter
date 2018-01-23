package io.cucumber.pro.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

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
        String value = get(key);
        if (value != null) return !value.matches("false|no");
        return notSetValue;
    }

    public int getInt(String key, int notSetValue) {
        return parseInt(get(key, Integer.toString(notSetValue)));
    }

    // Use by loaders
    public void set(String key, String value) {
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
                config = config.getChild(key.toLowerCase());
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
                Config childConfig = config.getChild(key.toLowerCase());
                if (childConfig == null) {
                    childConfig = new Config();
                    config.setConfig(key, childConfig);
                }
                config = childConfig;
            }
        }
    }

    private Config getChild(String key) {
        return configByKey.get(key);
    }

    public String toYaml(String rootKey) {
        try {
            StringBuilder out = new StringBuilder();
            this.print(0, rootKey, out);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void print(int depth, String rootKey, Appendable out) throws IOException {
        for (Map.Entry<String, String> entry : valueByKey.entrySet()) {
            if (rootKey == null || rootKey.equals(entry.getKey())) {
                indent(depth, out);
                out.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        for (Map.Entry<String, Config> entry : configByKey.entrySet()) {
            if (rootKey == null || rootKey.equals(entry.getKey())) {
                indent(depth, out);
                out.append(entry.getKey()).append(":\n");
                Config config = entry.getValue();
                config.print(depth + 1, null, out);
            }
        }
    }

    private void indent(int depth, Appendable out) throws IOException {
        for (int i = 0; i < depth; i++) {
            out.append("  ");
        }
    }

    private List<String> toPath(String key) {
        return Arrays.asList(key.replace('_', '.').toLowerCase(Locale.ENGLISH).split("\\."));
    }
}
