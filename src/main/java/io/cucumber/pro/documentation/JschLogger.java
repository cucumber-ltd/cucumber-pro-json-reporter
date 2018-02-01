package io.cucumber.pro.documentation;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Logger;

class JschLogger implements com.jcraft.jsch.Logger {
    private final Logger logger;

    public JschLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isEnabled(int level) {
        return true;
    }

    @Override
    public void log(int level, String message) {
        switch (level) {
            case com.jcraft.jsch.Logger.DEBUG:
                logger.log(Logger.Level.DEBUG, message);
                break;
            case com.jcraft.jsch.Logger.INFO:
                logger.log(Logger.Level.INFO, message);
                break;
            case com.jcraft.jsch.Logger.WARN:
                logger.log(Logger.Level.WARN, message);
                break;
            case com.jcraft.jsch.Logger.ERROR:
                logger.log(Logger.Level.ERROR, message);
                break;
            case com.jcraft.jsch.Logger.FATAL:
                logger.log(Logger.Level.FATAL, message);
                break;
            default:
                logger.log(Logger.Level.FATAL, message);
                throw new CucumberException("Unexpected log level: " + level);
        }
    }
}
