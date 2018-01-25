package io.cucumber.pro.config;

public class NullValue implements Value {
    @Override
    public String getString() {
        return "";
    }

    @Override
    public Boolean getBoolean() {
        return false;
    }

    @Override
    public Integer getInt() {
        return 0;
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
