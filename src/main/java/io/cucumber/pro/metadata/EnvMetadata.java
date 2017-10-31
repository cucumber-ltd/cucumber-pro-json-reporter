package io.cucumber.pro.metadata;

import java.util.Map;

public class EnvMetadata implements Metadata {

    public static final String CUCUMBER_PRO_PROJECT_NAME = "CUCUMBER_PRO_PROJECT_NAME";

    public static Metadata create() {
        return new EnvMetadata(System.getenv());
    }
    private final Map<String, String> env;

    public EnvMetadata(Map<String, String> env) {
        this.env = env;
    }

    @Override
    public String getProjectName() {
        return env.get(CUCUMBER_PRO_PROJECT_NAME);
    }
}
