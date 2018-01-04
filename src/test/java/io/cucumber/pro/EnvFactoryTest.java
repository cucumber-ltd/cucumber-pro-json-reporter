package io.cucumber.pro;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class EnvFactoryTest {
    @Test
    public void duplicates_bamboo_prefixed_env_vars_with_prefixless_copy() {
        Env env = EnvFactory.create(new HashMap<String, String>(){{
            put("bamboo_FOO", "X");
        }});

        assertEquals("X", env.get("bamboo_FOO"));
        assertEquals("X", env.get("FOO"));
    }

    @Test
    public void prefixless_env_var_takes_precendence() {
        Env env = EnvFactory.create(new HashMap<String, String>(){{
            put("bamboo_BAR", "X");
            put("BAR", "Y");
        }});

        assertEquals("Y", env.get("BAR"));
    }
}