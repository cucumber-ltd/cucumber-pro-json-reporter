package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;

public interface ConfigLoader {
    void load(Config config);
}
