package io.cucumber.pro;

public interface Logger {
    Logger System = new Logger() {
        @Override
        public void info(String message, Object... args) {
            java.lang.System.out.print("INFO: ");
            java.lang.System.out.format(message, args);
        }

        @Override
        public void warn(String message, Object... args) {
            java.lang.System.err.print("WARNING: ");
            java.lang.System.err.format(message, args);
        }

        @Override
        public void error(String message, Object... args) {
            java.lang.System.err.print("ERROR: ");
            java.lang.System.err.format(message, args);
        }
    };

    public void info(String message, Object... args);

    public void warn(String message, Object... args);

    public void error(String message, Object... args);
}
