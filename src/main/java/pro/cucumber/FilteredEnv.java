package pro.cucumber;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

class FilteredEnv {
    private String patternStr;
    private Map<String, String> env;

    FilteredEnv(String mask, Map<String, String> env) {
        this.patternStr = MessageFormat.format(".*({0}).*", mask);
        this.env = env;
    }

    private Map<String, String> clean() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> entry: this.env.entrySet()) {
            if (!Pattern.matches(this.patternStr, entry.getKey()))
                result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public String toString() {
        StringBuilder envb = new StringBuilder();

        Map<String, String> clean = clean();
        List<String> sortedKeys = new ArrayList<>(clean.keySet());
        Collections.sort(sortedKeys);

        for (String key : sortedKeys) {
            envb.append(key).append("=").append(clean.get(key)).append("\n");
        }
        return envb.toString();
    }
}
