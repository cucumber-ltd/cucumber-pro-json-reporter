package io.cucumber.pro;

import io.cucumber.pro.config.Config;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;

public class SystemLoggerTest {
    @Test
    public void is_info_level_by_default() {
        Config config = createConfig();
        Logger.SystemLogger logger = new Logger.SystemLogger(config);
        assertEquals(Logger.Level.INFO, logger.level);
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

    @Test
    public void writes_to_file_when_logfile_defined() throws IOException {
        Config config = createConfig();
        config.set(Keys.CUCUMBERPRO_LOGFILE, "target/cpro.log");
        Logger.SystemLogger logger = new Logger.SystemLogger(config);
        logger.log(Logger.Level.ERROR, "THE ERROR");
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("target/cpro.log"), "UTF-8"));
        String line = in.readLine();
        assertEquals("ERROR: THE ERROR", line);
    }
}