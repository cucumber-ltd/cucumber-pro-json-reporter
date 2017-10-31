package io.cucumber.pro.metadata;

import java.util.HashMap;
import java.util.Map;

public class EnvMetadataTest extends MetadataContract {
    @Override
    protected Metadata createMetadata() {
        Map<String, String> env = new HashMap<String, String>() {{
            put("CUCUMBER_PRO_PROJECT_NAME", "cucumber-pro-publisher-jvm");
        }};
        return new EnvMetadata(env);
    }
}
