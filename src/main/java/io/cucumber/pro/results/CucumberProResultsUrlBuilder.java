package io.cucumber.pro.results;

import io.cucumber.pro.Env;
import io.cucumber.pro.URITemplate;

import java.util.HashMap;

class CucumberProResultsUrlBuilder {
    private static final String DEFAULT_CUCUMBER_PRO_URL = "https://app.cucumber.pro/";

    static String buildCucumberProUrl(final Env env, final String projectName, final String revision) {
        String template = "{cucumberProUrl}tests/results/{projectName}/{revision}";
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("cucumberProUrl", getCucumberProUrl(env));
            put("projectName", projectName);
            put("revision", revision);
        }});
    }

    private static String getCucumberProUrl(Env env) {
        String cucumberProUrl = env.get("CUCUMBER_PRO_URL", DEFAULT_CUCUMBER_PRO_URL);
        if (!cucumberProUrl.endsWith("/")) cucumberProUrl += "/";
        return cucumberProUrl;
    }
}
