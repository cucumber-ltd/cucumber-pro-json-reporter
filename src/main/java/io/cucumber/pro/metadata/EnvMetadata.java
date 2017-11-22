package io.cucumber.pro.metadata;

import io.cucumber.pro.Env;

public class EnvMetadata implements Metadata {

    private static final String ENV_TRAVIS_REPO_SLUG = "TRAVIS_REPO_SLUG";
    private static final String[] ENV_PROJECT_NAME_VARS = new String[]{
            Env.CUCUMBER_PRO_PROJECT_NAME, // overrides everything
            "bamboo_planRepository_name", // https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html
            "CIRCLE_PROJECT_REPONAME", // https://circleci.com/docs/2.0/env-vars/#circleci-environment-variable-descriptions
            ENV_TRAVIS_REPO_SLUG, // https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
    };
    private final Env env;

    EnvMetadata(Env env) {
        this.env = env;
    }

    @Override
    public String getProjectName() {
        for (String envVar : ENV_PROJECT_NAME_VARS) {
            String value = env.get(envVar, null);
            if (value == null) continue;
            if (envVar.equals(ENV_TRAVIS_REPO_SLUG)) {
                return value.split("/")[1];
            } else {
                return value;
            }
        }
        return null;
    }
}
