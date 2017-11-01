package io.cucumber.pro.metadata;

import io.cucumber.pro.Env;

import static java.util.Arrays.asList;

public class MetadataFactory {
    public static Metadata create(Env env) {
        return new ChainedMetadata(asList(new EnvMetadata(env), YamlMetadata.create()));
    }
}
