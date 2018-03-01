package io.cucumber.pro.environment;

import io.cucumber.pro.config.Config;

public enum CIEnvironment {

    // https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html
    BAMBOO("bamboo_planRepository_revision", "bamboo_repository_git_branch", null, "bamboo_planRepository_name"),
    // https://circleci.com/docs/2.0/env-vars/#circleci-environment-variable-descriptions
    CIRCLE("CIRCLE_SHA1", "CIRCLE_BRANCH", "CIRCLE_TAG", "CIRCLE_PROJECT_REPONAME"),
    // https://wiki.jenkins.io/display/JENKINS/Git+Plugin#GitPlugin-Environmentvariables
    // https://wiki.jenkins.io/display/JENKINS/Git+Tag+Message+Plugin
    JENKINS("GIT_COMMIT", "GIT_BRANCH", "GIT_TAG_NAME", null),
    // https://docs.microsoft.com/en-us/vsts/build-release/concepts/definitions/build/variables?tabs=batch#predefined-variables
    TFS("BUILD_SOURCEVERSION", "BUILD_SOURCEBRANCHNAME", null, "SYSTEM_TEAMPROJECT"),
    // https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
    // Can't use TRAVIS_REPO_SLUG because it has both org and name (org/name)
    TRAVIS("TRAVIS_COMMIT", "TRAVIS_BRANCH", null, null),
    // http://devcenter.wercker.com/docs/environment-variables/available-env-vars
    WERCKER("WERCKER_GIT_COMMIT", "WERCKER_GIT_BRANCH", null, "WERCKER_GIT_REPOSITORY");

    public final String revisionVar;
    public final String branchVar;
    public final String tagVar;
    public final String projectNameVar;

    CIEnvironment(String revisionVar, String branchVar, String tagVar, String projectNameVar) {
        this.revisionVar = revisionVar;
        this.branchVar = branchVar;
        this.tagVar = tagVar;
        this.projectNameVar = projectNameVar;
    }

    public static CIEnvironment detect(Config config) {
        for (CIEnvironment ciEnvironment : CIEnvironment.values()) {
            if (ciEnvironment.detected(config)) return ciEnvironment;
        }
        return null;
    }

    public String getRevision(Config config) {
        return config.getString(revisionVar);
    }

    public String getBranch(Config config) {
        return config.getString(branchVar);
    }

    public String getTag(Config config) {
        return tagVar == null || config.isNull(tagVar) ? null : config.getString(tagVar);
    }

    public String getProjectName(Config config) {
        return projectNameVar == null || config.isNull(projectNameVar) ? null : config.getString(projectNameVar);
    }

    public boolean detected(Config config) {
        boolean revision = !config.isNull(revisionVar);
        boolean branch = !config.isNull(branchVar);
        return revision && branch;
    }
}
