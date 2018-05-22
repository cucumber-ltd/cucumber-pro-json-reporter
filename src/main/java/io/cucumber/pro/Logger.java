package io.cucumber.pro;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.config.Config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public interface Logger {
    void log(Level level, String message, Object... args);

    RuntimeException log(Exception e, String message);

    enum Level {
        DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);
        private final int value;

        Level(int value) {
            this.value = value;
        }
    }

    class SystemLogger implements Logger {
        public final Level level;

        private final PrintWriter stdout;
        private final PrintWriter stderr;
        private final PrintWriter fileWriter;

        SystemLogger(Config config) {
            String name = config.getString(Keys.CUCUMBERPRO_LOGGING).toUpperCase();
            Level level;
            try {
                level = Level.valueOf(name);
            } catch (IllegalArgumentException e) {
                level = Level.WARN;
            }
            try {
                stdout = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));
                stderr = new PrintWriter(new OutputStreamWriter(System.err, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new CucumberException(e);
            }
            if (config.getString(Keys.CUCUMBERPRO_LOGFILE) != null) {
                try {
                    fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(config.getString(Keys.CUCUMBERPRO_LOGFILE)), "UTF-8"));
                } catch (IOException e) {
                    throw new CucumberException("Failed to create Cucumber Pro log file " + config.getString(Keys.CUCUMBERPRO_LOGFILE), e);
                }
            } else {
                fileWriter = null;
            }
            this.level = level;
        }

        @Override
        public void log(Level level, String message, Object... args) {
            if (level.value >= this.level.value) {
                PrintWriter console = level.value >= Level.WARN.value ? stderr : stdout;
                log(console, level, message, args);
                if (fileWriter != null) {
                    log(fileWriter, level, message, args);
                }
            }
        }

        private void log(PrintWriter printWriter, Level level, String message, Object[] args) {
            printWriter.print(level);
            printWriter.print(": ");
            printWriter.format(message, args);
            printWriter.print("\n");
            printWriter.flush();
        }

        @Override
        public RuntimeException log(Exception e, String message) {
            log(Level.ERROR, message);
            return new CucumberException(message, e);
        }
    }
}
