package io.cucumber.pro.activation;

import java.util.Map;

public class EnvActivation implements Activation {
    private static final String[] ENV_ACTIVATION_VARS = new String[]{
            "CUCUMBER_PRO_PUBLISH",
            "BUILD_NUMBER",
            "CIRCLE_BUILD_NUM",
            "TRAVIS_JOB_NUMBER",
            "bamboo_buildNumber"
    };
    private final Map<String, String> env;

    EnvActivation(Map<String, String> env) {
        this.env = env;
    }

    public static Activation create() {
        return new EnvActivation(System.getenv());
    }

    @Override
    public boolean isActive() {
        for (String envVar : ENV_ACTIVATION_VARS) {
            String value = env.get(envVar);
            if (value != null) {
                return !value.toLowerCase().matches("false|no");
            }
        }
        return false;

    }
}
