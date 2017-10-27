package io.cucumber.proreporter.gitcli;

import io.cucumber.proreporter.RevisionProvider;
import io.cucumber.proreporter.RevisionProviderContract;

import java.nio.file.Path;

public class GitCliRevisionProviderTest extends RevisionProviderContract {
    @Override
    protected RevisionProvider makeRevisionProvider(Path rootPath) {
        return new GitCliRevisionProvider(rootPath);
    }
}
