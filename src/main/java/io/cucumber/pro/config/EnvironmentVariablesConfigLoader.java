package io.cucumber.pro.config;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class EnvironmentVariablesConfigLoader implements ConfigLoader {
    private final Pattern keyPattern;
    private final Map<String, String> env;

    public EnvironmentVariablesConfigLoader(String keyPattern) {
        this(keyPattern, System.getenv());
    }

    public EnvironmentVariablesConfigLoader(String keyPattern, Map<String, String> env) {
        this.keyPattern = Pattern.compile(keyPattern);
        this.env = env;
    }

    @Override
    public void load(Config config) {
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            key = key.replace('_', '.').toLowerCase(Locale.ENGLISH);
            if (keyPattern.matcher(key).lookingAt()) {
                config.setIn(key, entry.getValue());
            }
        }
    }
}
