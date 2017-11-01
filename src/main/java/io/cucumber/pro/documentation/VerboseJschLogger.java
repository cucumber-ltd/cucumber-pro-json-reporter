package io.cucumber.pro.documentation;

import com.jcraft.jsch.Logger;

import java.util.HashMap;
import java.util.Map;

class VerboseJschLogger implements Logger {
    private static Map<Integer, String> LEVELS = new HashMap<Integer, String>() {{
        put(Logger.DEBUG, "DEBUG");
        put(Logger.INFO, "INFO");
        put(Logger.WARN, "WARN");
        put(Logger.ERROR, "ERROR");
        put(Logger.FATAL, "FATAL");
    }};

    @Override
    public boolean isEnabled(int level) {
        return true;
    }

    @Override
    public void log(int level, String message) {
        System.out.format("%s: %s\n", LEVELS.get(level), message);
    }
}
