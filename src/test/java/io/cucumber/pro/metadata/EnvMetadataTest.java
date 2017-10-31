package io.cucumber.pro.metadata;

import io.cucumber.pro.Env;

import java.util.HashMap;
import java.util.Map;

public class EnvMetadataTest extends MetadataContract {
    @Override
    protected Metadata createMetadata() {
        Env env = new Env(new HashMap<String, String>() {{
            put("CUCUMBER_PRO_PROJECT_NAME", "cucumber-pro-plugin-jvm");
        }});
        return new EnvMetadata(env);
    }
}
