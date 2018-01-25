package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.ConfigLoader;

public class NullConfigLoader implements ConfigLoader {
    @Override
    public void load(Config config) {
    }
}
