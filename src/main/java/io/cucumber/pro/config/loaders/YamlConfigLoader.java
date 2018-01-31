package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.RealValue;
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


    public YamlConfigLoader(Reader reader) {
        this.map = YAML.load(reader);
    }

    public static void load(String[] yamlFileNames, Config config) {
        for (String yamlFileName : yamlFileNames) {
            getConfigLoader(yamlFileName).load(config);
        }
    }

    private static ConfigLoader getConfigLoader(String yamlFileName) {
        File file = new File(yamlFileName);
        if (!file.isFile()) return new NullConfigLoader();

        try {
            Reader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
            return new YamlConfigLoader(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load(Config config) {
        populate(config, map);
    }

    private void populate(Config config, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String property = entry.getKey();
            property = property.replaceAll("_", "");
            Object value = entry.getValue();
            if (value == null) {
                config.setNull(property);
            } else if (value instanceof String) {
                config.setValue(property, RealValue.fromString((String) value));
            } else if (value instanceof Boolean) {
                config.setValue(property, RealValue.fromBoolean((Boolean) value));
            } else if (value instanceof Integer) {
                config.setValue(property, RealValue.fromInteger((Integer) value));
            } else if (value instanceof Map) {
//                Config childConfig = new Config();
                Config childConfig = config.getChild(property);
//                config.setConfig(property, childConfig);
                populate(childConfig, (Map<String, Object>) value);
            } else {
                throw new RuntimeException(String.format("Unsupported YAML type: %s (%s)", value, value.getClass()));
            }
        }

    }
}
