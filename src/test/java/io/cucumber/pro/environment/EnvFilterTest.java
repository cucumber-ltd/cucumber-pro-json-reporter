package io.cucumber.pro.environment;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.EnvironmentVariablesConfigLoader;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;

public class EnvFilterTest {
    @Test
    public void filters_and_sorts_keys() {
        Map<String, String> env = new HashMap<String, String>() {{
            put("my_secret__token", "abcd");
            put("MY_SECRET_TOKEN", "abcd");
            put("A_KEY_TO_A_DOOR", "clef");
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
            put("PASSWORD_A", "drowssap");
        }};

        Config config = createConfig();
        new EnvironmentVariablesConfigLoader(env).load(config);
        EnvFilter envFilter = new EnvFilter(config);

        Map<String, String> expectedEnv = new HashMap<String, String>() {{
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
        }};

        assertEquals(expectedEnv, envFilter.filter(env));
    }

    @Test
    public void allows_overriding_mask() {
        Map<String, String> env = new HashMap<String, String>() {{
            put("CUCUMBERPRO_ENVMASK", "KEY|TOKEN"); // But not SECRET|PASSWORD
            put("my_secret__token", "abcd");
            put("MY_SECRET_TOKEN", "abcd");
            put("A_KEY_TO_A_DOOR", "clef");
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
            put("PASSWORD_A", "drowssap");
        }};

        Config config = createConfig();
        new EnvironmentVariablesConfigLoader(env).load(config);
        EnvFilter envFilter = new EnvFilter(config);

        Map<String, String> expectedEnv = new HashMap<String, String>() {{
            put("CUCUMBERPRO_ENVMASK", "KEY|TOKEN");
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
            put("PASSWORD_A", "drowssap");
        }};
        assertEquals(expectedEnv, envFilter.filter(env));
    }
}