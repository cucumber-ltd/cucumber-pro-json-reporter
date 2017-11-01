package io.cucumber.pro.activation;

import io.cucumber.pro.Env;

public class EnvActivation implements Activation {
    private static final String[] ENV_ACTIVATION_VARS = new String[]{
            "CUCUMBER_PRO_PUBLISH",
            "BUILD_NUMBER",
            "CIRCLE_BUILD_NUM",
            "TRAVIS_JOB_NUMBER",
            "bamboo_buildNumber"
    };
    private final Env env;

    public EnvActivation(Env env) {
        this.env = env;
    }

    @Override
    public boolean isActive() {
        for (String envVar : ENV_ACTIVATION_VARS) {
            if (env.getBoolean(envVar, false)) return true;
        }
        return false;

    }
}
