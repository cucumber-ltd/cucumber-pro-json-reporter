package io.cucumber.pro.environment;

import io.cucumber.pro.Keys;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.revision.RevisionProvider;

public enum CIEnvironment {

    BAMBOO(Keys.bamboo_planRepository_revision, Keys.bamboo_repository_git_branch, Keys.bamboo_buildNumber),
    CIRCLE(Keys.CIRCLE_SHA1, Keys.CIRCLE_BRANCH, Keys.CIRCLE_BUILD_NUM),
    JENKINS(Keys.GIT_COMMIT, Keys.GIT_BRANCH, Keys.BUILD_NUMBER),
    TRAVIS(Keys.TRAVIS_COMMIT, Keys.TRAVIS_BRANCH, Keys.TRAVIS_JOB_NUMBER);

    private final String revisionVar;
    private final String branchVar;
    private final String buildNumberVar;

    CIEnvironment(String revisionVar, String branchVar, String buildNumberVar) {
        this.revisionVar = revisionVar;
        this.branchVar = branchVar;
        this.buildNumberVar = buildNumberVar;
    }

    public static CIEnvironment detect(Config config) {
        for (CIEnvironment ciEnvironment : CIEnvironment.values()) {
            if (ciEnvironment.detected(config)) return ciEnvironment;
        }
        return null;
    }

    public boolean detected(Config config) {
        boolean revision = !config.isNull(revisionVar);
        boolean branch = !config.isNull(branchVar);
        boolean buildNumber = !config.isNull(buildNumberVar);
        return revision && branch && buildNumber;
    }

    public RevisionProvider getRevisionProvider(final Config config) {
        return new RevisionProvider() {
            @Override
            public String getRevision() {
                return config.getString(revisionVar);
            }

            @Override
            public String getBranch() {
                return config.getString(branchVar);
            }
        };
    }
}
