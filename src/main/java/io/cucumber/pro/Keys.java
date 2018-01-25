package io.cucumber.pro;

import io.cucumber.pro.config.Config;

public class Keys {
    public static final String CUCUMBER_PRO_TOKEN = "cucumber.pro.token";
    public static final String CUCUMBER_PRO_BASE_URL = "cucumber.pro.base.url";
    public static final String CUCUMBER_PRO_GIT_SSH_PORT = "cucumber.pro.git.ssh.port";
    public static final String CUCUMBER_PRO_GIT_HOST = "cucumber.pro.git.host";
    public static final String CUCUMBER_PRO_GIT_HOST_KEY = "cucumber.pro.git.host.key";
    public static final String CUCUMBER_PRO_GIT_DEBUG = "cucumber.pro.git.debug";
    public static final String CUCUMBER_PRO_GIT_PUBLISH = "cucumber.pro.git.publish";
    public static final String CUCUMBER_PRO_IGNORE_CONNECTION_ERROR = "cucumber.pro.ignore.connection.error";
    public static final String CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS = "cucumber.pro.connection.timeout.millis";
    public static final String CUCUMBER_PRO_ENV_MASK = "cucumber.pro.env.mask";
    public static final String CUCUMBER_PRO_SOURCE_REMOTE_NAME = "cucumber.pro.source.remote.name";
    public static final String CUCUMBER_PRO_FETCH_FROM_SOURCE = "cucumber.pro.fetch.from.source";
    public static final String CUCUMBER_PRO_LOG_LEVEL = "cucumber.pro.log.level";
    public static final String CUCUMBER_PROFILE_NAME = "cucumber.profile.name";

    // Revisions
    public static final String bamboo_planRepository_revision = "bamboo_planRepository_revision";
    public static final String CIRCLE_SHA1 = "CIRCLE_SHA1";
    public static final String GIT_COMMIT = "GIT_COMMIT";
    public static final String TRAVIS_COMMIT = "TRAVIS_COMMIT";

    // Activation (via CI)
    public static final String CUCUMBER_PRO_PUBLISH = "cucumber.pro.publish";
    public static final String BUILD_NUMBER = "BUILD_NUMBER";
    public static final String CIRCLE_BUILD_NUM = "CIRCLE_BUILD_NUM";
    public static final String TRAVIS_JOB_NUMBER = "TRAVIS_JOB_NUMBER";
    public static final String bamboo_buildNumber = "bamboo_buildNumber";

    // Project name
    public static final String CUCUMBER_PRO_PROJECT_NAME = "cucumber.pro.project.name";
    // https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html
    public static final String bamboo_planRepository_name = "bamboo_planRepository_name";
    // https://circleci.com/docs/2.0/env-vars/#circleci-environment-variable-descriptions
    public static final String CIRCLE_PROJECT_REPONAME = "CIRCLE_PROJECT_REPONAME";
    // https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
    public static final String TRAVIS_REPO_SLUG = "TRAVIS_REPO_SLUG";

    // Defaults
    private static final String DEFAULT_CUCUMBER_PRO_URL = "https://app.cucumber.pro/";
    private static final String DEFAULT_ENV_MASK = "SECRET|KEY|TOKEN|PASSWORD";
    private static final String DEFAULT_CUCUMBER_PROFILE_NAME = "cucumber-jvm-unspecified-profile";
    private static final String DEFAULT_GIT_HOST = "git.cucumber.pro";
    private static final String DEFAULT_SOURCE_REMOTE_NAME = "origin";

    public static Config createConfig() {
        Config config = new Config();
        config.setNull(CUCUMBER_PRO_TOKEN);
        config.set(CUCUMBER_PRO_BASE_URL, DEFAULT_CUCUMBER_PRO_URL);
        config.set(CUCUMBER_PRO_GIT_SSH_PORT, 22);
        config.set(CUCUMBER_PRO_GIT_HOST, DEFAULT_GIT_HOST);
        config.setNull(CUCUMBER_PRO_GIT_HOST_KEY);
        config.set(CUCUMBER_PRO_GIT_DEBUG, false);
        config.set(CUCUMBER_PRO_GIT_PUBLISH, false);
        config.set(CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, true);
        config.set(CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS, 5000);
        config.set(CUCUMBER_PRO_ENV_MASK, DEFAULT_ENV_MASK);
        config.set(CUCUMBER_PRO_SOURCE_REMOTE_NAME, DEFAULT_SOURCE_REMOTE_NAME);
        config.set(CUCUMBER_PRO_FETCH_FROM_SOURCE, true);
        config.set(CUCUMBER_PRO_LOG_LEVEL, "WARN");
        config.set(CUCUMBER_PROFILE_NAME, DEFAULT_CUCUMBER_PROFILE_NAME);

        config.setNull(bamboo_planRepository_revision);
        config.setNull(CIRCLE_SHA1);
        config.setNull(GIT_COMMIT);
        config.setNull(TRAVIS_COMMIT);
        config.setNull(bamboo_planRepository_revision);

        config.setNull(CUCUMBER_PRO_PUBLISH);
        config.setNull(BUILD_NUMBER);
        config.setNull(CIRCLE_BUILD_NUM);
        config.setNull(TRAVIS_JOB_NUMBER);
        config.setNull(bamboo_buildNumber);

        config.setNull(CUCUMBER_PRO_PROJECT_NAME);
        config.setNull(bamboo_planRepository_name);
        config.setNull(CIRCLE_PROJECT_REPONAME);
        config.setNull(TRAVIS_REPO_SLUG);
        return config;
    }
}
