package io.cucumber.pro.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

public class YamlConfigLoader implements ConfigLoader {
    private static final Yaml YAML = new Yaml();
    private final Map<String, Object> map;

    public static final String[] YAML_FILE_NAMES = new String[]{
            "cucumber.yml",
            ".cucumber.yml",
            ".cucumber/cucumber.yml",
            ".cucumberpro.yml"
    };

    static ConfigLoader create() {
        for (String yamlFileName : YAML_FILE_NAMES) {
            File file = new File(yamlFileName);
            if (file.isFile()) {
                Reader reader = null;
                try {
                    reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
                    return new YamlConfigLoader(reader);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new NullConfigLoader();
    }

    public YamlConfigLoader(Reader reader) {
        this.map = YAML.load(reader);
    }

    @Override
    public void load(Config config) {
        populate(config, map);
    }

    private void populate(Config config, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            key = key.replaceAll("_", "");
            Object value = entry.getValue();
            if (value instanceof String) {
                config.setValue(key, new RealValue((String) value));
            } else if (value instanceof Map) {
                Config childConfig = new Config();
                config.setConfig(key, childConfig);
                populate(childConfig, (Map<String, Object>) value);
            }
        }

    }
}
