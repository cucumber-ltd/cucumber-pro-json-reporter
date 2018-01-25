package io.cucumber.pro;

import io.cucumber.pro.config.Config;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;

public class SystemLoggerTest {
    @Test
    public void is_warn_level_by_default() {
        Config config = createConfig();
        Logger.SystemLogger logger = new Logger.SystemLogger(config);
        assertEquals(Logger.Level.WARN, logger.level);
    }

    @Test
    public void ignores_log_level_case() {
        Config config = createConfig();
        config.set(Keys.CUCUMBERPRO_LOGGING, "deBug");
        Logger.SystemLogger logger = new Logger.SystemLogger(config);
        assertEquals(Logger.Level.DEBUG, logger.level);
    }

    @Test
    public void defaults_to_WARN_when_unknown_log_level_is_set() {
        Config config = createConfig();
        config.set(Keys.CUCUMBERPRO_LOGGING, "Bogus");
        Logger.SystemLogger logger = new Logger.SystemLogger(config);
        assertEquals(Logger.Level.WARN, logger.level);
    }
}