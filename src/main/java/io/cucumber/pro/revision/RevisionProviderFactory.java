package io.cucumber.pro.revision;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.revision.jgit.JGitRevisionProvider;

public class RevisionProviderFactory {
    public static RevisionProvider create() {
        String revisionProviderClassName = System.getenv("CUCUMBER_PRO_REVISION_PROVIDER");
        if (revisionProviderClassName == null) revisionProviderClassName = JGitRevisionProvider.class.getName();
        try {
            Class<RevisionProvider> providerClass = (Class<RevisionProvider>) Thread.currentThread().getContextClassLoader().loadClass(revisionProviderClassName);
            return providerClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new CucumberException(e);
        }
    }
}
