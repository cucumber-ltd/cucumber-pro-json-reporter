package io.cucumber.pro.config;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConfigTest {
    @Test
    public void gets_and_sets_value() {
        Config config = new Config();
        config.set("name", "progress");
        assertEquals("progress", config.getString("name"));
    }

    @Test
    public void gets_boolean() {
        Config config = new Config();
        config.set("a", true);
        config.set("b", false);

        assertTrue(config.getBoolean("a"));
        assertFalse(config.getBoolean("b"));
    }

    @Test
    public void gets_deep_value() {
        Config root = new Config();

        Config one = new Config();
        root.setConfig("one", one);

        Config two = new Config();
        one.setConfig("two", two);

        two.set("hello", "world");
        assertEquals("world", root.getString("one.two.hello"));
    }

    @Test
    public void throws_exception_when_no_value_set() {
        Config config = new Config();
        try {
            config.getString("not.set");
            fail();
        } catch (UndefinedKeyException expected) {
            assertEquals("No such key: not.set", expected.getMessage());
        }
    }

    @Test
    public void has_yaml_representation() {
        Config config = new Config();
        config.set("a.b.c.d.e", "1");
        config.set("aa.x.y.z", "X");
        config.set("a.c.d.e", "3");
        config.set("a.d.e", "4");

        String expected = "" +
                "a:\n" +
                "  b:\n" +
                "    c:\n" +
                "      d:\n" +
                "        e: 1\n" +
                "  c:\n" +
                "    d:\n" +
                "      e: 3\n" +
                "  d:\n" +
                "    e: 4\n" +
                "";
        assertEquals(expected, config.toYaml("a"));
    }
}
