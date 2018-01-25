package io.cucumber.pro.environment;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.config.Config;

import static io.cucumber.pro.Keys.CIRCLE_PROJECT_REPONAME;
import static io.cucumber.pro.Keys.CUCUMBERPRO_PROJECTNAME;
import static io.cucumber.pro.Keys.TRAVIS_REPO_SLUG;
import static io.cucumber.pro.Keys.bamboo_planRepository_name;

public class ProjectName {

    private static final String[] ENV_PROJECT_NAME_VARS = new String[]{
            CUCUMBERPRO_PROJECTNAME,
            bamboo_planRepository_name,
            CIRCLE_PROJECT_REPONAME,
            TRAVIS_REPO_SLUG,
    };
    private final Config config;

    public ProjectName(Config config) {
        this.config = config;
    }

    public String getProjectName() {
        for (String envVar : ENV_PROJECT_NAME_VARS) {
            if (config.isNull(envVar)) continue;
            String value = config.getString(envVar);
            if (envVar.equals(TRAVIS_REPO_SLUG)) {
                return value.split("/")[1];
            } else {
                return value;
            }
        }
        throw new CucumberException("Couldn't detect project name. Please define " + CUCUMBERPRO_PROJECTNAME);
    }
}
