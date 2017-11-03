package io.cucumber.pro;

import java.util.Map;

import static java.lang.Integer.parseInt;

public class Env {
    private final Map<String, String> env;

    public Env(Map<String, String> env) {
        this.env = env;
    }

    public String get(String key) {
        return env.get(key);
    }

    public String get(String key, String defaultValue) {
        String value = env.get(key);
        if (value != null) return value;
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = env.get(key);
        if (value != null) {
            return !value.toLowerCase().matches("false|no");
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        String value = env.get(key);
        if (value != null) {
            return parseInt(value.toLowerCase());
        }
        return defaultValue;
    }
}
