package io.cucumber.pro;

public interface Logger {
    void log(Level level, String message, Object... args);


    public enum Level {
        DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);
        private final int value;

        Level(int value) {
            this.value = value;
        }
    }

    class SystemLogger implements Logger {
        private final Level level;

        public SystemLogger(Env env) {
            this.level = Level.valueOf(env.get(Env.CUCUMBER_PRO_LOG_LEVEL, Level.WARN.toString()));
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

    }
}
