package io.cucumber.pro.activation;

import io.cucumber.pro.Env;
import io.cucumber.pro.config.Config;

public class EnvActivation implements Activation {
    private static final String[] ACTIVATION_ENV_VARS = new String[]{
            Env.CUCUMBER_PRO_PUBLISH,
            "BUILD_NUMBER",
            "CIRCLE_BUILD_NUM",
            "TRAVIS_JOB_NUMBER",
            "bamboo_buildNumber"
    };
    private final Config config;

    public EnvActivation(Config config) {
        this.config = config;
    }

    @Override
    public boolean isActive() {
        for (String envVar : ACTIVATION_ENV_VARS) {
            if (config.getBoolean(envVar, false)) return true;
        }
        return false;

    }
}
