package io.cucumber.pro.revision.jgit;

import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.RevisionProviderContract;

import java.nio.file.Path;

public class JGitRevisionProviderTest extends RevisionProviderContract {
    @Override
    protected RevisionProvider makeRevisionProvider(Path rootPath) {
        return new JGitRevisionProvider(rootPath);
    }
}
