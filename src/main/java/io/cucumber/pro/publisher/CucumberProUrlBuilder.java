package io.cucumber.pro.publisher;

import java.util.HashMap;
import java.util.Map;

public class CucumberProUrlBuilder {
    private static final String DEFAULT_CUCUMBER_PRO_URL = "https://app.cucumber.pro/";

    protected static String buildCucumberProUrl(Map<String, String> env, final String projectName, final String revision) {
        String cucumberProUrl = env.get("CUCUMBER_PRO_URL");
        if (cucumberProUrl == null) cucumberProUrl = DEFAULT_CUCUMBER_PRO_URL;
        if (!cucumberProUrl.endsWith("/")) cucumberProUrl += "/";
        String template = String.format("%stests/results/{projectName}/{revision}", cucumberProUrl);
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("projectName", projectName);
            put("revision", revision);
        }});
    }
}
