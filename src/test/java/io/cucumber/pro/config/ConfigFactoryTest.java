package io.cucumber.pro.config;

import org.junit.Test;

public class ConfigFactoryTest {
    @Test
    public void create_cofig() {
        try {
            Config config = ConfigFactory.create("^(?:cucumber)");
            System.out.println(config.get("user.dir"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
