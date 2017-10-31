package io.cucumber.pro.publisher;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CucumberProUrlBuilderTest {

    @Test
    public void builds_url_for_saas_by_default() {
        HashMap<String, String> env = new HashMap<>();
        String url = CucumberProUrlBuilder.buildCucumberProUrl(env, "my-project", "my-revision");
        assertEquals("https://app.cucumber.pro/tests/results/my-project/my-revision", url);
    }

    @Test
    public void builds_url_for_appliance_when_env_var_is_set() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("CUCUMBER_PRO_URL", "http://example.com:8090");
        }};
        String url = CucumberProUrlBuilder.buildCucumberProUrl(env, "my-project", "my-revision");
        assertEquals("http://example.com:8090/tests/results/my-project/my-revision", url);
    }

}
