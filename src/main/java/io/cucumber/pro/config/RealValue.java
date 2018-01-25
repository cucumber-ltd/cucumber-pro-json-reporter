package io.cucumber.pro.config;

import static java.lang.Integer.parseInt;

public class RealValue implements Value {
    private String value;

    public RealValue(String value) {
        if(value == null) throw new NullPointerException("value cannot be null");
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public Boolean getBoolean() {
        return !value.matches("false|no|off");
    }

    @Override
    public Integer getInt() {
        return parseInt(value);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    public static Value fromString(String value) {
        return new RealValue(value);
    }

    public static Value fromInt(int value) {
        return new RealValue(Integer.toString(value));
    }

    public static Value fromBoolean(boolean value) {
        return new RealValue(Boolean.toString(value));
    }
}
