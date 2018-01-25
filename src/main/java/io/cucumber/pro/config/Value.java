package io.cucumber.pro.config;

public interface Value {
    String getString();

    Boolean getBoolean();

    Integer getInt();

    boolean isNull();
}
