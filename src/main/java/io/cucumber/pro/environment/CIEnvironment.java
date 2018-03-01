package io.cucumber.pro.environment;

import java.util.Map;

public class CIEnvironment {

    private final String ciName;
    private final String revision;
    private final String branch;
    private final String tag;
    private final String projectName;

    CIEnvironment(String ciName, String revision, String branch, String tag, String projectName) {
        this.ciName = ciName;
        this.revision = revision;
        this.branch = branch;
        this.tag = tag;
        this.projectName = projectName;
    }

    public String getCiName() {
        return ciName;
    }

    public String getRevision() {
        return revision;
    }

    public String getBranch() {
        return branch;
    }

    public String getTag() {
        return tag;
    }

    public String getProjectName() {
        return projectName;
    }

    public static CIEnvironment detect(Map<String, String> env) {
        CIEnvironment ci;
        if ((ci = detectBamboo(env)) != null) return ci;
        if ((ci = detectCircle(env)) != null) return ci;
        if ((ci = detectJenkins(env)) != null) return ci;
        if ((ci = detectTfs(env)) != null) return ci;
        if ((ci = detectTravis(env)) != null) return ci;
        if ((ci = detectWercker(env)) != null) return ci;
        return null;
    }

    // https://confluence.atlassian.com/bamboo/bamboo-variables-289277087.html
    private static CIEnvironment detectBamboo(Map<String, String> env) {
        String revision = env.get("bamboo_planRepository_revision");
        String branch = env.get("bamboo_repository_git_branch");
        if (revision == null || branch == null) return null;
        String tag = null;
        String projectName = env.get("bamboo_planRepository_name");
        return new CIEnvironment(
                "Bamboo",
                revision,
                branch,
                tag,
                projectName
        );
    }

    // https://circleci.com/docs/2.0/env-vars/#circleci-environment-variable-descriptions
    private static CIEnvironment detectCircle(Map<String, String> env) {
        String revision = env.get("CIRCLE_SHA1");
        String branch = env.get("CIRCLE_BRANCH");
        if (revision == null || branch == null) return null;
        String tag = env.get("CIRCLE_TAG");
        String projectName = env.get("CIRCLE_PROJECT_REPONAME");
        return new CIEnvironment(
                "Circle CI",
                revision,
                branch,
                tag,
                projectName
        );
    }

    // https://wiki.jenkins.io/display/JENKINS/Git+Plugin#GitPlugin-Environmentvariables
    private static CIEnvironment detectJenkins(Map<String, String> env) {
        String revision = env.get("GIT_COMMIT");
        String branch = env.get("GIT_BRANCH");
        if (revision == null || branch == null) return null;
        // https://wiki.jenkins.io/display/JENKINS/Git+Tag+Message+Plugin
        String tag = env.get("GIT_TAG_NAME");
        String projectName = null;
        return new CIEnvironment(
                "Jenkins",
                revision,
                branch,
                tag,
                projectName
        );
    }

    // https://docs.microsoft.com/en-us/vsts/build-release/concepts/definitions/build/variables?tabs=batch#predefined-variables
    private static CIEnvironment detectTfs(Map<String, String> env) {
        String revision = env.get("BUILD_SOURCEVERSION");
        String branch = env.get("BUILD_SOURCEBRANCHNAME");
        if (revision == null || branch == null) return null;
        String tag = null;
        String projectName = env.get("SYSTEM_TEAMPROJECT");
        return new CIEnvironment(
                "TFS",
                revision,
                branch,
                tag,
                projectName
        );
    }

    // https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
    private static CIEnvironment detectTravis(Map<String, String> env) {
        String revision = env.get("TRAVIS_COMMIT");
        String branch = env.get("TRAVIS_BRANCH");
        if (revision == null || branch == null) return null;
        String tag = null;
        String repoSlug = env.get("TRAVIS_REPO_SLUG");
        String projectName = repoSlug.split("/")[1];
        return new CIEnvironment(
                "Travis CI",
                revision,
                branch,
                tag,
                projectName
        );
    }

    // http://devcenter.wercker.com/docs/environment-variables/available-env-vars
    private static CIEnvironment detectWercker(Map<String, String> env) {
        String revision = env.get("WERCKER_GIT_COMMIT");
        String branch = env.get("WERCKER_GIT_BRANCH");
        if (revision == null || branch == null) return null;
        String tag = null;
        String projectName = env.get("WERCKER_GIT_REPOSITORY");
        return new CIEnvironment(
                "Wercker",
                revision,
                branch,
                tag,
                projectName
        );
    }

}
