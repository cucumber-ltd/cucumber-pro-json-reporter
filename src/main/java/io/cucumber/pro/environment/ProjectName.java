package io.cucumber.pro.environment;

import io.cucumber.pro.Logger;
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
    private final Logger logger;

    public ProjectName(Config config, Logger logger) {
        this.config = config;
        this.logger = logger;
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
        String message = "Couldn't detect project name. Please define " + CUCUMBERPRO_PROJECTNAME;
        throw logger.log(null, message);
    }
}
