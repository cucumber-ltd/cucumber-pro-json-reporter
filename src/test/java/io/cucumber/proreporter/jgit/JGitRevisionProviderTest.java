package io.cucumber.proreporter.jgit;

import io.cucumber.proreporter.RevisionProvider;
import io.cucumber.proreporter.RevisionProviderContract;

import java.nio.file.Path;

public class JGitRevisionProviderTest extends RevisionProviderContract {
    @Override
    protected RevisionProvider makeRevisionProvider(Path rootPath) {
        return new JGitRevisionProvider(rootPath);
    }
}
