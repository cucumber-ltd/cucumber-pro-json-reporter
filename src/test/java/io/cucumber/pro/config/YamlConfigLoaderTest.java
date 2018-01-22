package io.cucumber.pro.config;

import java.io.StringReader;

public class YamlConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new YamlConfigLoader(new StringReader("" +
                "cucumber:\n" +
                "  format: progress\n"));
    }
}
