package io.cucumber.pro;

import io.cucumber.pro.config.Config;

public class Keys {
    public static final String CUCUMBERPRO_URL = "cucumberpro.url";
    public static final String CUCUMBERPRO_TOKEN = "cucumberpro.token";
    public static final String CUCUMBERPRO_CONNECTION_IGNOREERROR = "cucumberpro.connection.ignoreerror";
    public static final String CUCUMBERPRO_CONNECTION_TIMEOUT = "cucumberpro.connection.timeout";
    public static final String CUCUMBERPRO_ENVMASK = "cucumberpro.envmask";
    public static final String CUCUMBERPRO_LOGGING = "cucumberpro.logging";

    // Project name
    public static final String CUCUMBERPRO_PROJECTNAME = "cucumberpro.projectname";
    // https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html
    public static final String bamboo_planRepository_name = "bamboo_planRepository_name";
    // https://circleci.com/docs/2.0/env-vars/#circleci-environment-variable-descriptions
    public static final String CIRCLE_PROJECT_REPONAME = "CIRCLE_PROJECT_REPONAME";
    // https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
    public static final String TRAVIS_REPO_SLUG = "TRAVIS_REPO_SLUG";

    public static Config createConfig() {
        Config config = new Config();
        config.setNull(CUCUMBERPRO_TOKEN);
        config.set(CUCUMBERPRO_URL, "https://app.cucumber.pro/");
        config.set(CUCUMBERPRO_CONNECTION_IGNOREERROR, true);
        config.set(CUCUMBERPRO_CONNECTION_TIMEOUT, 5000);
        config.set(CUCUMBERPRO_ENVMASK, "SECRET|KEY|TOKEN|PASSWORD");
        config.set(CUCUMBERPRO_LOGGING, "INFO");

        config.setNull(CUCUMBERPRO_PROJECTNAME);
        config.setNull(bamboo_planRepository_name);
        config.setNull(CIRCLE_PROJECT_REPONAME);
        config.setNull(TRAVIS_REPO_SLUG);
        return config;
    }
}
