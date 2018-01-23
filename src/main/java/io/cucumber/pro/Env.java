package io.cucumber.pro;

public class Env {
    public static final String CUCUMBER_PRO_TOKEN = "cucumber.pro.token";
    public static final String CUCUMBER_PRO_BASE_URL = "cucumber.pro.base.url";
    public static final String CUCUMBER_PRO_GIT_SSH_PORT = "cucumber.pro.git.ssh.port";
    public static final String CUCUMBER_PRO_GIT_HOST = "cucumber.pro.git.host";
    public static final String CUCUMBER_PRO_GIT_HOST_KEY = "cucumber.pro.git.host.key";
    public static final String CUCUMBER_PRO_GIT_DEBUG = "cucumber.pro.git.debug";
    public static final String CUCUMBER_PRO_GIT_PUBLISH = "cucumber.pro.git.publish";
    public static final String CUCUMBER_PRO_PUBLISH = "cucumber.pro.publish";
    public static final String CUCUMBER_PRO_PROJECT_NAME = "cucumber.pro.project.name";
    public static final String CUCUMBER_PRO_IGNORE_CONNECTION_ERROR = "cucumber.pro.ignore.connection.error";
    public static final String CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS = "cucumber.pro.connection.timeout.millis";
    public static final String CUCUMBER_PRO_ENV_MASK = "cucumber.pro.env.mask";
    public static final String CUCUMBER_PRO_SOURCE_REMOTE_NAME = "cucumber.pro.source.remote.name";
    public static final String CUCUMBER_PRO_FETCH_FROM_SOURCE = "cucumber.pro.fetch.from.source";
    public static final String CUCUMBER_PRO_LOG_LEVEL = "cucumber.pro.log.level";

    public static final String bamboo_planRepository_revision = "bamboo_planRepository_revision";
    public static final String CIRCLE_SHA1 = "CIRCLE_SHA1";
    public static final String GIT_COMMIT = "GIT_COMMIT";
    public static final String TRAVIS_COMMIT = "TRAVIS_COMMIT";
    // TODO: Use cucumber.pro prefix.
    // TODO: How do we avoid splitting profile.name?
    //   By using camelCase!
    public static final String CUCUMBER_PROFILE_NAME = "cucumber.profile.name";
}
