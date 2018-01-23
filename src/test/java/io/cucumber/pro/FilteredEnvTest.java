package io.cucumber.pro;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.EnvironmentVariablesConfigLoader;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class FilteredEnvTest {
    @Test
    public void filters_and_sorts_keys() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("my_secret__token", "abcd");
            put("MY_SECRET_TOKEN", "abcd");
            put("A_KEY_TO_A_DOOR", "clef");
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
            put("PASSWORD_A", "drowssap");
        }};
        Config config = new Config();
        new EnvironmentVariablesConfigLoader(env).load(config);
        FilteredEnv filteredEnv = new FilteredEnv(env, config);
        String actual = filteredEnv.toString();
        assertEquals("ALPHA=BETA\nDOO=dar\nFOO=BAR\n", actual);
    }

    @Test
    public void allows_overriding_mask() {
        HashMap<String, String> env = new HashMap<String, String>() {{
            put("CUCUMBER_PRO_ENV_MASK", "KEY|TOKEN");
            put("my_secret__token", "abcd");
            put("MY_SECRET_TOKEN", "abcd");
            put("A_KEY_TO_A_DOOR", "clef");
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
            put("PASSWORD_A", "drowssap");
        }};
        Config config = new Config();
        new EnvironmentVariablesConfigLoader(env).load(config);
        FilteredEnv filteredEnv = new FilteredEnv(env, config);
        String actual = filteredEnv.toString();
        assertEquals("ALPHA=BETA\nCUCUMBER_PRO_ENV_MASK=KEY|TOKEN\nDOO=dar\nFOO=BAR\nPASSWORD_A=drowssap\n", actual);
    }
}