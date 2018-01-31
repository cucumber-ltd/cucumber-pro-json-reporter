package io.cucumber.pro.environment;

import io.cucumber.pro.Keys;
import io.cucumber.pro.config.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EnvFilter {
    private final Pattern maskPattern;

    public EnvFilter(Config config) {
        String mask = config.getString(Keys.CUCUMBERPRO_ENVMASK);
        this.maskPattern = Pattern.compile(String.format(".*(%s).*", mask), Pattern.CASE_INSENSITIVE);
    }

    public Map<String, String> filter(Map<String, String> env) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            if (!maskPattern.matcher(entry.getKey()).matches())
                result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
