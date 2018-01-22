package io.cucumber.pro.config;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bamboo prefixes all environment variables with `bamboo_`. This class
 * strips them off.
 */
public class BambooEnvironmentVariablesConfigLoader implements ConfigLoader {
    private static Pattern BAMBOO_PATTERN = Pattern.compile("^bamboo_(.+)");
    private final Map<String, String> env;

    public BambooEnvironmentVariablesConfigLoader() {
        this(System.getenv());
    }

    public BambooEnvironmentVariablesConfigLoader(Map<String, String> env) {
        this.env = env;
    }

    @Override
    public void load(Config config) {
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            Matcher matcher = BAMBOO_PATTERN.matcher(key);
            if (matcher.lookingAt()) {
                config.setIn(matcher.group(1).replace('_', '.').toLowerCase(Locale.ENGLISH), entry.getValue());
                config.setIn(key.replace('_', '.').toLowerCase(Locale.ENGLISH), entry.getValue());
            }
        }
    }
}
