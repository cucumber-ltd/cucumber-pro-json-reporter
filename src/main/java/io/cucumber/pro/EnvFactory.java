package io.cucumber.pro;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvFactory {
    private static Pattern BAMBOO_PATTERN = Pattern.compile("^bamboo_(.+)");

    public static Env create(Map<String, String> env) {
        Map<String, String> mappedEnv = new HashMap<>(env);
        for (Map.Entry<String, String> entry : env.entrySet()) {
            Matcher matcher = BAMBOO_PATTERN.matcher(entry.getKey());
            if (matcher.matches()) {
                String envVarWithoutPrefix = matcher.group(1);
                if (!env.containsKey(envVarWithoutPrefix)) {
                    mappedEnv.put(envVarWithoutPrefix, entry.getValue());
                }
            }
        }
        return new Env(mappedEnv);
    }
}
