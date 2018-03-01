package io.cucumber.pro.environment;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bamboo prefixes all environment variables with `bamboo_`. This class
 * strips them off.
 */
public class BambooEnvironmentVariables {
    private static Pattern BAMBOO_PATTERN = Pattern.compile("^bamboo_(.+)");

    public Map<String, String> convert(Map<String, String> env) {
        Map<String, String> result = new HashMap<>();

        SortedSet<String> vars = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String v1, String v2) {
                if (BAMBOO_PATTERN.matcher(v1).lookingAt()) return -1;
                if (BAMBOO_PATTERN.matcher(v2).lookingAt()) return 1;
                return 0;
            }
        });
        vars.addAll(env.keySet());

        for (String key : vars) {
            Matcher matcher = BAMBOO_PATTERN.matcher(key);
            if (matcher.lookingAt()) {
                String value = env.get(key);
                String strippedVar = matcher.group(1);
                result.put(strippedVar, value);
            }
        }
        result.putAll(env);
        return result;
    }
}
