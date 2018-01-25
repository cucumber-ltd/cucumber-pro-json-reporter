package io.cucumber.pro.environment;

import io.cucumber.pro.config.Config;

import static io.cucumber.pro.Keys.BUILD_NUMBER;
import static io.cucumber.pro.Keys.CIRCLE_BUILD_NUM;
import static io.cucumber.pro.Keys.CUCUMBER_PRO_PUBLISH;
import static io.cucumber.pro.Keys.TRAVIS_JOB_NUMBER;
import static io.cucumber.pro.Keys.bamboo_buildNumber;

public class CI {
    private static final String[] CI_ENV_VARS = new String[]{
            CUCUMBER_PRO_PUBLISH,
            BUILD_NUMBER,
            CIRCLE_BUILD_NUM,
            TRAVIS_JOB_NUMBER,
            bamboo_buildNumber
    };
    private final Config config;

    public CI(Config config) {
        this.config = config;
    }

    public boolean isRunningInCi() {
        for (String envVar : CI_ENV_VARS) {
            if (!config.isNull(envVar)) return true;
        }
        return false;
    }
}
