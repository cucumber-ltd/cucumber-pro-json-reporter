package pro.cucumber;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FilteredEnvTest {
    @Test
    public void removesKeysMatchingMask() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("MY_SECRECT_TOKEN", "abcd");
        env.put("PASSWORD_A", "drowssap");
        env.put("A_KEY_TO_A_DOOR", "clef");
        env.put("FOO", "BAR");

        FilteredEnv filteredEnv = new FilteredEnv("PASSWORD|KEY|TOKEN", env);
        Map<String, String> actual = filteredEnv.clean();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("FOO", "BAR");

        assertEquals(expected, actual);
    }

    @Test
    public void convertsToString() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("FOO", "BAR");
        env.put("DOO", "dar");

        FilteredEnv filteredEnv = new FilteredEnv("PASSWORD|KEY|TOKEN", env);
        String actual = filteredEnv.toString();
        String[] lines = actual.split("\n");
        Arrays.sort(lines);
        actual = String.join("\n", lines);

        assertEquals("DOO=dar\nFOO=BAR", actual);
    }
}