package io.cucumber.pro.environment;

import java.util.HashMap;
import java.util.Map;
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
        for (String key : env.keySet()) {
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
