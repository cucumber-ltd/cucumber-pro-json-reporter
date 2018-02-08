package io.cucumber.pro.results;

import io.cucumber.pro.Keys;
import io.cucumber.pro.URITemplate;
import io.cucumber.pro.config.Config;

import java.util.HashMap;

class CucumberProResultsUrlBuilder {

    static String buildCucumberProUrl(final Config config, final String projectName) {
        String template = "{cucumberProUrl}tests/results/{projectName}";
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("cucumberProUrl", getCucumberProUrl(config));
            put("projectName", projectName);
        }});
    }

    private static String getCucumberProUrl(Config config) {
        String cucumberProUrl = config.getString(Keys.CUCUMBERPRO_URL);
        if (!cucumberProUrl.endsWith("/")) cucumberProUrl += "/";
        return cucumberProUrl;
    }
}
