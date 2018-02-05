package io.cucumber.pro;

import io.cucumber.pro.config.Config;
import io.cucumber.pro.config.loaders.ConfigLoader;

import java.util.Map;

public class DeprecatedEnvironmentVariablesConfigLoader implements ConfigLoader {
    private static final String CUCUMBERPRO_RESULTS_TOKEN = "CUCUMBERPRO_RESULTS_TOKEN";
    private static final String CUCUMBER_PRO_PROJECT_NAME = "CUCUMBER_PRO_PROJECT_NAME";
    private static final String CUCUMBER_PRO_BASE_URL = "CUCUMBER_PRO_BASE_URL";
    private static final String CUCUMBER_PRO_IGNORE_CONNECTION_ERROR = "CUCUMBER_PRO_IGNORE_CONNECTION_ERROR";
    private static final String CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS = "CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS";
    private static final String CUCUMBER_PRO_ENV_MASK = "CUCUMBER_PRO_ENV_MASK";
    private static final String CUCUMBER_PRO_LOG_LEVEL = "CUCUMBER_PRO_LOG_LEVEL";
    private static final String CUCUMBER_PROFILE_NAME = "CUCUMBER_PROFILE_NAME";
    private final Map<String, String> env;

    public DeprecatedEnvironmentVariablesConfigLoader() {
        this(System.getenv());
    }

    public DeprecatedEnvironmentVariablesConfigLoader(Map<String, String> env) {
        this.env = env;
    }

    @Override
    public void load(Config config) {
        map(config, CUCUMBERPRO_RESULTS_TOKEN, Keys.CUCUMBERPRO_TOKEN);
        map(config, CUCUMBER_PRO_PROJECT_NAME, Keys.CUCUMBERPRO_PROJECTNAME);
        map(config, CUCUMBER_PRO_BASE_URL, Keys.CUCUMBERPRO_URL);
        map(config, CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, Keys.CUCUMBERPRO_CONNECTION_IGNOREERROR);
        map(config, CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS, Keys.CUCUMBERPRO_CONNECTION_TIMEOUT);
        map(config, CUCUMBER_PRO_ENV_MASK, Keys.CUCUMBERPRO_ENVMASK);
        map(config, CUCUMBER_PRO_LOG_LEVEL, Keys.CUCUMBERPRO_LOGGING);
        map(config, CUCUMBER_PROFILE_NAME, null);
    }

    private void map(Config config, String deprecatedEnvVar, String newKey) {
        if (env.containsKey(deprecatedEnvVar)) {
            if (newKey != null) {
                System.err.format("*** WARNING: The %s environment variable is deprecated and will not be supported from version 2.0.0 of the Cucumber Pro plugin. Please use %s instead\n", deprecatedEnvVar, newKey);
                config.set(newKey, env.get(deprecatedEnvVar));
            } else {
                System.err.format("*** WARNING: The %s environment variable is no longer supported\n", deprecatedEnvVar);
            }
        }
    }
}
