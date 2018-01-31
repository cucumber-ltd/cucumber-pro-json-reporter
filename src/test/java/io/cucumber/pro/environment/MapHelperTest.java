package io.cucumber.pro.environment;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapHelperTest {
    @Test
    public void prints_sorted_env_with_newline() {
        Map<String, String> env = new HashMap<String, String>() {{
            put("FOO", "BAR");
            put("ALPHA", "BETA");
            put("DOO", "dar");
        }};
        assertEquals("ALPHA=BETA\nDOO=dar\nFOO=BAR\n", MapHelper.toEnvString(env));
    }
}