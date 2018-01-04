package io.cucumber.pro.revision;

import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;

public class RevisionProviderFactory {
    private static final String[] REVISION_ENV_VARS = new String[]{
            Env.bamboo_planRepository_revision, // Bamboo
            Env.CIRCLE_SHA1,                    // Circle
            Env.GIT_COMMIT,                     // Jenkins / Hudson
            Env.TRAVIS_COMMIT                   // Travis
    };

    public static RevisionProvider create(final Env env, Logger logger) {
        for (final String envVar : REVISION_ENV_VARS) {
            if (env.get(envVar) != null) {
                return new RevisionProvider() {
                    @Override
                    public String getRevision() {
                        return env.get(envVar);
                    }
                };
            }
        }
        return new GitRevisionProvider(logger);
    }
}
