package io.cucumber.pro.results;

import io.cucumber.pro.Keys;
import io.cucumber.pro.config.Config;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;

public class CucumberProUrlBuilderTest {

    @Test
    public void builds_url_for_saas_by_default() {
        Config config = createConfig();
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, "my-project");
        assertEquals("https://app.cucumber.pro/tests/results/my-project", url);
    }

    @Test
    public void builds_url_for_appliance_when_env_var_is_set() {
        Config config = createConfig();
        config.set(Keys.CUCUMBERPRO_URL, "http://example.com:8090");
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(config, "my-project");
        assertEquals("http://example.com:8090/tests/results/my-project", url);
    }

}
