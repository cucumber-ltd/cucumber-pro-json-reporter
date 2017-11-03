package io.cucumber.pro;

import java.util.ArrayList;
import java.util.List;

public class TestLogger implements Logger {
    public final List<String> info = new ArrayList<>();
    public final List<String> warn = new ArrayList<>();
    public final List<String> error = new ArrayList<>();

    @Override
    public void info(String message, Object... args) {
        info.add(String.format(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        warn.add(String.format(message, args));
    }

    @Override
    public void error(String message, Object... args) {
        error.add(String.format(message, args));
    }
}
