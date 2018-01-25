package io.cucumber.pro.config;

public class UndefinedKeyException extends RuntimeException {
    public UndefinedKeyException(String key) {
        super("No such key: " + key);
    }
}
