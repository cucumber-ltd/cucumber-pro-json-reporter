package pro.cucumber;

import org.junit.Test;

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
}