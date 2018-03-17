package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Keys;
import io.cucumber.pro.config.Config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class CucumberProResultsUrlBuilder {

    static String buildCucumberProUrl(final Config config, final String projectName) {
        return getCucumberProUrl(config) + "tests/results/" + encodeURIComponent(projectName);
    }

    private static String getCucumberProUrl(Config config) {
        String cucumberProUrl = config.getString(Keys.CUCUMBERPRO_URL);
        if (!cucumberProUrl.endsWith("/")) cucumberProUrl += "/";
        return cucumberProUrl;
    }

    private static String encodeURIComponent(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("%21", "!")
                    .replaceAll("%27", "'")
                    .replaceAll("%28", "(")
                    .replaceAll("%29", ")")
                    .replaceAll("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new CucumberException(e);
        }
    }
}
