package io.cucumber.pro.results;

import io.cucumber.pro.Env;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CucumberProUrlBuilderTest {

    @Test
    public void builds_url_for_saas_by_default() {
        Env env = new Env(new HashMap<String, String>());
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(env, "my-project", "my-revision");
        assertEquals("https://app.cucumber.pro/tests/results/my-project/my-revision", url);
    }

    @Test
    public void builds_url_for_appliance_when_env_var_is_set() {
        Env env = new Env(new HashMap<String, String>() {{
            put("CUCUMBER_PRO_BASE_URL", "http://example.com:8090");
        }});
        String url = CucumberProResultsUrlBuilder.buildCucumberProUrl(env, "my-project", "my-revision");
        assertEquals("http://example.com:8090/tests/results/my-project/my-revision", url);
    }

}
