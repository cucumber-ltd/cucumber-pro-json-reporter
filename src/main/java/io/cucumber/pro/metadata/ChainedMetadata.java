package io.cucumber.pro.metadata;

import java.util.List;

import static java.util.Arrays.asList;

public class ChainedMetadata implements Metadata {

    private final List<Metadata> chain;

    public ChainedMetadata(List<Metadata> chain) {
        this.chain = chain;
    }

    public static Metadata create() {
        return new ChainedMetadata(asList(EnvMetadata.create(), YamlMetadata.create()));
    }

    @Override
    public String getProjectName() {
        for (Metadata metadata : chain) {
            String result = metadata.getProjectName();
            if (result != null) return result;
        }
        return null;
    }
}
