package io.cucumber.pro.metadata;

import java.util.List;

import static io.cucumber.pro.metadata.EnvMetadata.CUCUMBER_PRO_PROJECT_NAME;
import static io.cucumber.pro.metadata.YamlMetadata.PROJECT_NAME_FIELD;
import static io.cucumber.pro.metadata.YamlMetadata.YAML_FILE_NAME;
import static java.util.Arrays.asList;

public class ChainedMetadata implements Metadata {

    public static Metadata create() {
        return new ChainedMetadata(asList(EnvMetadata.create(), YamlMetadata.create()));
    }

    private final List<Metadata> chain;

    public ChainedMetadata(List<Metadata> chain) {
        this.chain = chain;
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
