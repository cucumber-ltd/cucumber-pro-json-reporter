package io.cucumber.pro.revision;

import io.cucumber.pro.Env;

public class BambooEnvGitRevisionProvider implements RevisionProvider {

    private final Env env;

    public BambooEnvGitRevisionProvider(Env env) {
        this.env = env;
    }

    @Override
    public String getRevision() {
        return env.get(Env.bamboo_planRepository_revision);
    }
}
