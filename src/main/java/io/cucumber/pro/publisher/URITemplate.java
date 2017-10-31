package io.cucumber.pro.publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a subset of https://tools.ietf.org/html/rfc6570
 */
class URITemplate {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{([^}]+)}");
    private final String template;

    URITemplate(String template) {
        this.template = template;
    }

    String expand(Map<String, String> values) {
        Matcher matcher = PARAMETER_PATTERN.matcher(template);
        StringBuffer format = new StringBuffer();
        List<String> arguments = new ArrayList<>();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            matcher.appendReplacement(format, "%s");
            String argument = values.get(variableName);
            if (argument == null) {
                throw new RuntimeException(String.format("Missing argument \"%s\". Template: %s Arguments: %s", variableName, template, values));
            }
            arguments.add(argument);
        }
        Object[] args = arguments.toArray(new String[arguments.size()]);
        return String.format(format.toString(), args);
    }
}
