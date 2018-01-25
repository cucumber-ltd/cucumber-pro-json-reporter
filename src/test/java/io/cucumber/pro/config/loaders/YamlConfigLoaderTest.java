package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class YamlConfigLoaderTest extends ConfigLoaderContract {
    @Override
    protected ConfigLoader makeConfigLoader() {
        return new YamlConfigLoader(new StringReader("" +
                "cucumber:\n" +
                "  format: progress\n"));
    }

    @Test
    public void removes_underscores_from_keys() {
        Config config = new Config();
        ConfigLoader configLoader = new YamlConfigLoader(new StringReader("" +
                "cucumber:\n" +
                "  f_or_mat_: progress\n"));
        configLoader.load(config);

        assertEquals("progress", config.getString("cucumber.format"));
    }

    @Test
    public void roundtips() {
        String yaml = "" +
                "cucumberpro:\n" +
                "  cucumberprofile: cucumber-jvm-unspecified-profile\n" +
                "  envmask: SECRET|KEY|TOKEN|PASSWORD\n" +
                "  logging: debug\n" +
                "  url: https://app.cucumber.pro/\n" +
                "  connection:\n" +
                "    ignoreerror: true\n" +
                "    timeout: 5000\n" +
                "  git:\n" +
                "    hostkey:\n" +
                "    hostname: git.cucumber.pro\n" +
                "    publish: false\n" +
                "    sshport: 22\n" +
                "    source:\n" +
                "      fetch: true\n" +
                "      remote: origin\n" +
                "  project:\n" +
                "    name:\n" +
                "  results:\n" +
                "    publish:\n" +
                "    token:\n";

        Config config = new Config();
        ConfigLoader configLoader = new YamlConfigLoader(new StringReader(yaml));
        configLoader.load(config);

        assertEquals(yaml, config.toYaml("cucumberpro"));
    }
}
