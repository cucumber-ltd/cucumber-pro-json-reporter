package io.cucumber.pro;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.config.Config;

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

        public SystemLogger(Config config) {
            String name = config.getString(Keys.CUCUMBERPRO_LOGGING).toUpperCase();
            Level level;
            try {
                level = Level.valueOf(name);
            } catch (IllegalArgumentException e) {
                level = Level.WARN;
            }
            this.level = level;
        }

        @Override
        public void log(Level level, String message, Object... args) {
            if (level.value >= this.level.value) {
                java.lang.System.out.print(level);
                java.lang.System.out.print(": ");
                java.lang.System.out.format(message, args);
                java.lang.System.out.print("\n");
            }
        }

        @Override
        public RuntimeException log(Exception e, String message) {
            log(Level.ERROR, message);
            return new CucumberException(message, e);
        }

    }
}
