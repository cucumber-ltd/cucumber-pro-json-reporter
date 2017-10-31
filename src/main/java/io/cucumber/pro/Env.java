package io.cucumber.pro;

import java.util.Map;

public class Env {
    private final Map<String, String> env;

    public Env(Map<String,String> env) {
        this.env = env;
    }

    public String get(String key, String defaultValue) {
        String value = env.get(key);
        if (value != null) return value;
        return defaultValue;
    }
}
