package pro.cucumber.gitcli;

import pro.cucumber.RevisionProvider;
import pro.cucumber.RevisionProviderContract;

import java.nio.file.Path;

public class GitCliRevisionProviderTest extends RevisionProviderContract {
    @Override
    protected RevisionProvider makeRevisionProvider(Path rootPath) {
        return new GitCliRevisionProvider(rootPath);
    }
}
