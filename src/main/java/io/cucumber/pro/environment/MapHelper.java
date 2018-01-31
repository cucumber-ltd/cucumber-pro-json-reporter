package io.cucumber.pro.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapHelper {
    public static String toEnvString(Map<String, String> env) {
        StringBuilder envb = new StringBuilder();

        List<String> sortedKeys = new ArrayList<>(env.keySet());
        Collections.sort(sortedKeys);

        for (String key : sortedKeys) {
            envb.append(key).append("=").append(env.get(key)).append("\n");
        }
        return envb.toString();
    }
}
