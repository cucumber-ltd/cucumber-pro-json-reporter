package io.cucumber.pro.metadata;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class YamlMetadata implements Metadata {

    public static final String YAML_FILE_NAME = ".cucumberpro.yml";
    public static final String PROJECT_NAME_FIELD = "project_name";
    private static final Yaml YAML = new Yaml();
    private final Map metadata;

    public YamlMetadata(Reader source) {
        metadata = YAML.load(source);
    }

    static Metadata create() {
        File yamlFile = new File(YAML_FILE_NAME);
        if (!yamlFile.isFile()) return new NullMetadata();
        try {
            return new YamlMetadata(new InputStreamReader(new FileInputStream(yamlFile), "UTF-8"));
        } catch (IOException e1) {
            return new NullMetadata();
        }
    }

    @Override
    public String getProjectName() {
        return (String) metadata.get(PROJECT_NAME_FIELD);
    }
}
