package io.cucumber.pro.config;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ConfigTest {
    @Test
    public void gets_and_sets_value() {
        Config config = new Config();
        config.setValue("name", "progress");
        assertEquals("progress", config.get("name"));
    }

    @Test
    public void gets_boolean() {
        Config config = new Config();
        config.setValue("a", "true");
        config.setValue("b", "false");

        assertTrue(config.getBoolean("a", false));
        assertFalse(config.getBoolean("b", false));

        assertTrue(config.getBoolean("a", true));
        assertFalse(config.getBoolean("b", true));

        assertFalse(config.getBoolean("c", false));
        assertTrue(config.getBoolean("c", true));
    }

    @Test
    public void gets_deep_value() {
        Config root = new Config();

        Config one = new Config();
        root.setConfig("one", one);

        Config two = new Config();
        one.setConfig("two", two);

        two.setValue("hello", "world");
        assertEquals("world", root.get("one.two.hello"));
    }

    @Test
    public void has_yaml_representation() {
        Config root = new Config();

        Config one = new Config();
        root.setConfig("one", one);

        Config two = new Config();
        one.setConfig("two", two);

        two.setValue("hello", "world");

        String expected = "" +
                "one:\n" +
                "  two:\n" +
                "    hello: world\n" +
                "";
        assertEquals(expected, root.toYaml());
    }
}
