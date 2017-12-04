package io.cucumber.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestLogger implements Logger {
    private final Map<Level, List<String>> messages = new HashMap<Level, List<String>>() {{
        put(Level.DEBUG, new ArrayList<String>());
        put(Level.INFO, new ArrayList<String>());
        put(Level.WARN, new ArrayList<String>());
        put(Level.ERROR, new ArrayList<String>());
        put(Level.FATAL, new ArrayList<String>());
    }};

    @Override
    public void log(Level level, String message, Object... args) {
        messages.get(level).add(String.format(message, args));
    }

    public List<String> getMessages(Level level) {
        return messages.get(level);
    }
}
