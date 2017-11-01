package io.cucumber.pro.revision;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Env;
import io.cucumber.pro.revision.jgit.JGitRevisionProvider;

public class RevisionProviderFactory {
    public static RevisionProvider create(Env env) {
        String revisionProviderClassName = env.get("CUCUMBER_PRO_REVISION_PROVIDER", JGitRevisionProvider.class.getName());
        try {
            Class<RevisionProvider> providerClass = (Class<RevisionProvider>) Thread.currentThread().getContextClassLoader().loadClass(revisionProviderClassName);
            return providerClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new CucumberException(e);
        }
    }
}
