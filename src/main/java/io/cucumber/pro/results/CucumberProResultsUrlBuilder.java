package io.cucumber.pro.results;

import io.cucumber.pro.Keys;
import io.cucumber.pro.URITemplate;
import io.cucumber.pro.config.Config;

import java.util.HashMap;

class CucumberProResultsUrlBuilder {

    static String buildCucumberProUrl(final Config config, final String projectName, final String revision) {
        String template = "{cucumberProUrl}tests/results/{projectName}/{revision}";
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("cucumberProUrl", getCucumberProUrl(config));
            put("projectName", projectName);
            put("revision", revision);
        }});
    }

    private static String getCucumberProUrl(Config config) {
        String cucumberProUrl = config.getString(Keys.CUCUMBER_PRO_BASE_URL);
        if (!cucumberProUrl.endsWith("/")) cucumberProUrl += "/";
        return cucumberProUrl;
    }
}
