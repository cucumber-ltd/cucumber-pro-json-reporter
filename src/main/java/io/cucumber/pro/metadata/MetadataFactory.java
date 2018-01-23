package io.cucumber.pro.metadata;

import io.cucumber.pro.config.Config;

import static java.util.Arrays.asList;

public class MetadataFactory {
    public static Metadata create(Config config) {
        return new ChainedMetadata(asList(new EnvMetadata(config), YamlMetadata.create()));
    }
}
