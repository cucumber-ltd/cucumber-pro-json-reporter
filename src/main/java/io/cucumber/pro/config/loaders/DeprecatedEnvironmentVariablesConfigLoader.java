package io.cucumber.pro.config.loaders;

import io.cucumber.pro.config.Config;

import java.util.Map;

public class DeprecatedEnvironmentVariablesConfigLoader implements ConfigLoader {
    private static final String CUCUMBER_PRO_TOKEN = "cucumber.pro.token";
    private static final String CUCUMBER_PRO_BASE_URL = "cucumber.pro.base.url";
    private static final String CUCUMBER_PRO_GIT_SSH_PORT = "cucumber.pro.git.ssh.port";
    private static final String CUCUMBER_PRO_GIT_HOST = "cucumber.pro.git.host";
    private static final String CUCUMBER_PRO_GIT_HOST_KEY = "cucumber.pro.git.host.key";
    private static final String CUCUMBER_PRO_GIT_DEBUG = "cucumber.pro.git.debug";
    private static final String CUCUMBER_PRO_GIT_PUBLISH = "cucumber.pro.git.publish";
    private static final String CUCUMBER_PRO_IGNORE_CONNECTION_ERROR = "cucumber.pro.ignore.connection.error";
    private static final String CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS = "cucumber.pro.connection.timeout.millis";
    private static final String CUCUMBER_PRO_ENV_MASK = "cucumber.pro.env.mask";
    private static final String CUCUMBER_PRO_SOURCE_REMOTE_NAME = "cucumber.pro.source.remote.name";
    private static final String CUCUMBER_PRO_FETCH_FROM_SOURCE = "cucumber.pro.fetch.from.source";
    private static final String CUCUMBER_PRO_LOG_LEVEL = "cucumber.pro.log.level";
    private static final String CUCUMBER_PROFILE_NAME = "cucumber.profile.name";

    public static final String CUCUMBER_PRO_PUBLISH = "cucumber.pro.publish";
    public static final String CUCUMBER_PRO_PROJECT_NAME = "cucumber.pro.project.name";

    private final Map<String, String> env;

    public DeprecatedEnvironmentVariablesConfigLoader() {
        this(System.getenv());
    }

    public DeprecatedEnvironmentVariablesConfigLoader(Map<String, String> env) {
        this.env = env;
    }

    @Override
    public void load(Config config) {
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            config.set(key, entry.getValue());
        }
    }
}
