package io.cucumber.pro.environment;

import io.cucumber.pro.config.Config;

public enum CIEnvironment {

    BAMBOO("bamboo_planRepository_revision", "bamboo_repository_git_branch", null),
    CIRCLE("CIRCLE_SHA1", "CIRCLE_BRANCH", "CIRCLE_TAG"),
    JENKINS("GIT_COMMIT", "GIT_BRANCH", "GIT_TAG_NAME"),
    TFS("BUILD_SOURCEVERSION", "BUILD_SOURCEBRANCHNAME", null),
    TRAVIS("TRAVIS_COMMIT", "TRAVIS_BRANCH", null),
    WERCKER("WERCKER_GIT_COMMIT", "WERCKER_GIT_BRANCH", null);

    public final String revisionVar;
    public final String branchVar;
    public final String tagVar;

    CIEnvironment(String revisionVar, String branchVar, String tagVar) {
        this.revisionVar = revisionVar;
        this.branchVar = branchVar;
        this.tagVar = tagVar;
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

    public boolean detected(Config config) {
        boolean revision = !config.isNull(revisionVar);
        boolean branch = !config.isNull(branchVar);
        return revision && branch;
    }
}
