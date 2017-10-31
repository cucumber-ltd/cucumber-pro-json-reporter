package io.cucumber.pro.metadata;

import java.util.Map;

public class EnvMetadata implements Metadata {

    public static final String ENV_TRAVIS_REPO_SLUG = "TRAVIS_REPO_SLUG";
    public static final String ENV_CUCUMBER_PRO_PROJECT_NAME = "CUCUMBER_PRO_PROJECT_NAME";
    public static final String[] ENV_PROJECT_NAME_VARS = new String[]{
            ENV_CUCUMBER_PRO_PROJECT_NAME, // overrides everything
            "bamboo_shortPlanName", // https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html
            "CIRCLE_PROJECT_REPONAME", // https://circleci.com/docs/2.0/env-vars/#circleci-environment-variable-descriptions
            ENV_TRAVIS_REPO_SLUG, // https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
    };
    private final Map<String, String> env;

    public EnvMetadata(Map<String, String> env) {
        this.env = env;
    }

    public static Metadata create() {
        return new EnvMetadata(System.getenv());
    }

    @Override
    public String getProjectName() {
        for (String envVar : ENV_PROJECT_NAME_VARS) {
            String value = env.get(envVar);
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
