package io.cucumber.pro;

import io.cucumber.pro.config.Config;

public class Keys {
    public static final String CUCUMBERPRO_TOKEN = "cucumberpro.token";
    public static final String CUCUMBERPRO_URL = "cucumberpro.url";
    public static final String CUCUMBERPRO_GIT_PUBLISH = "cucumberpro.git.publish";
    public static final String CUCUMBERPRO_GIT_SSHPORT = "cucumberpro.git.sshport";
    public static final String CUCUMBERPRO_GIT_HOSTNAME = "cucumberpro.git.hostname";
    public static final String CUCUMBERPRO_GIT_HOSTKEY = "cucumberpro.git.hostkey";
    public static final String CUCUMBERPRO_GIT_SOURCE_REMOTE = "cucumberpro.git.source.remote";
    public static final String CUCUMBERPRO_GIT_SOURCE_FETCH = "cucumberpro.git.source.fetch";
    public static final String CUCUMBERPRO_RESULTS_PUBLISH = "cucumberpro.results.publish";
    public static final String CUCUMBERPRO_CONNECTION_IGNOREERROR = "cucumberpro.connection.ignoreerror";
    public static final String CUCUMBERPRO_CONNECTION_TIMEOUT = "cucumberpro.connection.timeout";
    public static final String CUCUMBERPRO_ENVMASK = "cucumberpro.envmask";
    public static final String CUCUMBERPRO_LOGGING = "cucumberpro.logging";

    // Revisions
    public static final String bamboo_planRepository_revision = "bamboo_planRepository_revision";
    public static final String CIRCLE_SHA1 = "CIRCLE_SHA1";
    public static final String GIT_COMMIT = "GIT_COMMIT";
    public static final String TRAVIS_COMMIT = "TRAVIS_COMMIT";

    // CI detection
    public static final String BUILD_NUMBER = "BUILD_NUMBER";
    public static final String CIRCLE_BUILD_NUM = "CIRCLE_BUILD_NUM";
    public static final String TRAVIS_JOB_NUMBER = "TRAVIS_JOB_NUMBER";
    public static final String bamboo_buildNumber = "bamboo_buildNumber";

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
        config.set(CUCUMBERPRO_GIT_SSHPORT, 22);
        config.set(CUCUMBERPRO_GIT_HOSTNAME, "git.cucumber.pro");
        config.setNull(CUCUMBERPRO_GIT_HOSTKEY);
        config.set(CUCUMBERPRO_GIT_PUBLISH, false);
        config.set(CUCUMBERPRO_CONNECTION_IGNOREERROR, true);
        config.set(CUCUMBERPRO_CONNECTION_TIMEOUT, 5000);
        config.set(CUCUMBERPRO_ENVMASK, "SECRET|KEY|TOKEN|PASSWORD");
        config.set(CUCUMBERPRO_GIT_SOURCE_REMOTE, "origin");
        config.set(CUCUMBERPRO_GIT_SOURCE_FETCH, true);
        config.set(CUCUMBERPRO_LOGGING, "WARN");

        config.setNull(bamboo_planRepository_revision);
        config.setNull(CIRCLE_SHA1);
        config.setNull(GIT_COMMIT);
        config.setNull(TRAVIS_COMMIT);
        config.setNull(bamboo_planRepository_revision);

        config.setNull(CUCUMBERPRO_RESULTS_PUBLISH);
        config.setNull(BUILD_NUMBER);
        config.setNull(CIRCLE_BUILD_NUM);
        config.setNull(TRAVIS_JOB_NUMBER);
        config.setNull(bamboo_buildNumber);

        config.setNull(CUCUMBERPRO_PROJECTNAME);
        config.setNull(bamboo_planRepository_name);
        config.setNull(CIRCLE_PROJECT_REPONAME);
        config.setNull(TRAVIS_REPO_SLUG);
        return config;
    }
}
