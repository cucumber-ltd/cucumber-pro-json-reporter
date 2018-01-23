package io.cucumber.pro.metadata;

import io.cucumber.pro.Env;
import io.cucumber.pro.config.Config;

import java.util.HashMap;

public class EnvMetadataTest extends MetadataContract {
    @Override
    protected Metadata createMetadata() {
        Config config = new Config();
        config.set(Env.CUCUMBER_PRO_PROJECT_NAME, "cucumber-pro-plugin-jvm");
        return new EnvMetadata(config);
    }
}
