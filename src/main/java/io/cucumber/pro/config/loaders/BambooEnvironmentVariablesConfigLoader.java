package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.ConfigLoader;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
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
        SortedSet<String> variables = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String v1, String v2) {
                if (BAMBOO_PATTERN.matcher(v1).lookingAt()) return -1;
                if (BAMBOO_PATTERN.matcher(v2).lookingAt()) return 1;
                return 0;
            }
        });
        variables.addAll(env.keySet());

        for (String key : variables) {
            Matcher matcher = BAMBOO_PATTERN.matcher(key);
            if (matcher.lookingAt()) {
                String value = env.get(key);
                String strippedKey = matcher.group(1);
                config.set(strippedKey, value);
            }
        }
    }
}
