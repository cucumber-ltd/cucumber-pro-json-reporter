package pro.cucumber;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FilteredEnvTest {
    @Test
    public void filters_and_sorts_keys() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("MY_SECRET_TOKEN", "abcd");
        env.put("A_KEY_TO_A_DOOR", "clef");
        env.put("FOO", "BAR");
        env.put("ALPHA", "BETA");
        env.put("DOO", "dar");
        env.put("PASSWORD_A", "drowssap");

        FilteredEnv filteredEnv = new FilteredEnv("PASSWORD|KEY|TOKEN", env);
        String actual = filteredEnv.toString();
        assertEquals("ALPHA=BETA\nDOO=dar\nFOO=BAR\n", actual);
    }
}