package io.cucumber.pro;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class SystemLoggerTest {
    @Test
    public void ignores_log_level_case() {
        Logger.SystemLogger logger = new Logger.SystemLogger(new Env(new HashMap<String, String>() {{
            put(Env.CUCUMBER_PRO_LOG_LEVEL, "deBug");
        }}));
        assertEquals(Logger.Level.DEBUG, logger.level);
    }

    @Test
    public void defaults_to_WARN_when_unknown_log_level_is_set() {
        Logger.SystemLogger logger = new Logger.SystemLogger(new Env(new HashMap<String, String>() {{
            put(Env.CUCUMBER_PRO_LOG_LEVEL, "Bogus");
        }}));
        assertEquals(Logger.Level.WARN, logger.level);
    }
}