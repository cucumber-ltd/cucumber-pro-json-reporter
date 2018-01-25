package io.cucumber.pro.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Nested configuration. Keys are hierarchical.
 */
public class Config {
    private final Map<String, Value> valueByProperty = new HashMap<>();
    private final Map<String, Config> configByProperty = new HashMap<>();

    public String getString(String key) {
        return getIn(normalize(key)).getString();
    }

    public Boolean getBoolean(String key) {
        return getIn(normalize(key)).getBoolean();
    }

    public Integer getInteger(String key) {
        return getIn(normalize(key)).getInt();
    }

    public boolean isNull(String key) {
        return getIn(normalize(key)).isNull();
    }

    public void setNull(String key) {
        setIn(normalize(key), new NullValue());
    }

    public void set(String key, String value) {
        setIn(normalize(key), RealValue.fromString(value));
    }

    public void set(String key, int value) {
        setIn(normalize(key), RealValue.fromInt(value));
    }

    public void set(String key, boolean value) {
        setIn(normalize(key), RealValue.fromBoolean(value));
    }

    private void set(String key, Value value) {
        setIn(normalize(key), value);
    }

    // Use by loaders
    void setValue(String property, Value value) {
        this.valueByProperty.put(property.toLowerCase(), value);
    }

    void setConfig(String property, Config childConfig) {
        this.configByProperty.put(property.toLowerCase(), childConfig);
    }

    private Value getValue(String property) {
        return this.valueByProperty.get(property.toLowerCase());
    }

    private Value getIn(String normalizedKey) {
        List<String> path = toPath(normalizedKey);
        Config config = this;
        for (int i = 0; i < path.size(); i++) {
            String property = path.get(i);
            if (i == path.size() - 1) {
                Value value = config.getValue(property);
                if (value != null) return value;
                throw new UndefinedKeyException(normalizedKey);
            } else {
                config = config.getChild(property.toLowerCase());
                if (config == null) {
                    throw new UndefinedKeyException(normalizedKey);
                }
            }
        }
        throw new RuntimeException("path cannot be empty");
    }

    private void setIn(String normalizedKey, Value value) {
        List<String> path = toPath(normalizedKey);
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
        return configByProperty.get(key);
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
        for (Map.Entry<String, Value> entry : valueByProperty.entrySet()) {
            if (rootKey == null || rootKey.equals(entry.getKey())) {
                indent(depth, out);
                out.append(entry.getKey()).append(": ").append(entry.getValue().getString()).append("\n");
            }
        }
        for (Map.Entry<String, Config> entry : configByProperty.entrySet()) {
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
        return Arrays.asList(normalize(key).split("\\."));
    }

    private String normalize(String key) {
        return key.replace('_', '.').toLowerCase(Locale.ENGLISH);
    }
}
