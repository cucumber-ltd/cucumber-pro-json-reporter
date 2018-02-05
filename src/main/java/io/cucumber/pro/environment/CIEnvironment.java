package io.cucumber.pro.environment;

import io.cucumber.pro.config.Config;

public enum CIEnvironment {

    BAMBOO("bamboo_planRepository_revision", "bamboo_repository_git_branch"),
    CIRCLE("CIRCLE_SHA1", "CIRCLE_BRANCH"),
    JENKINS("GIT_COMMIT", "GIT_BRANCH"),
    TRAVIS("TRAVIS_COMMIT", "TRAVIS_BRANCH");

    public final String revisionVar;
    public final String branchVar;

    CIEnvironment(String revisionVar, String branchVar) {
        this.revisionVar = revisionVar;
        this.branchVar = branchVar;
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

    public boolean detected(Config config) {
        boolean revision = !config.isNull(revisionVar);
        boolean branch = !config.isNull(branchVar);
        return revision && branch;
    }
}
