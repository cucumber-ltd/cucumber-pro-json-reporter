package io.cucumber.pro.revision.git;

import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.RevisionProviderContract;

import java.nio.file.Path;

public class GitRevisionProviderTest extends RevisionProviderContract {
    @Override
    protected RevisionProvider makeRevisionProvider(Path rootPath) {
        return new GitRevisionProvider(rootPath);
    }
}
