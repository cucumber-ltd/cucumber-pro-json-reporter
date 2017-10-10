package pro.cucumber.jgit;

import pro.cucumber.RevisionProvider;
import pro.cucumber.RevisionProviderContract;
import pro.cucumber.gitcli.GitCliRevisionProvider;

import java.nio.file.Path;

public class JGitRevisionProviderTest extends RevisionProviderContract {
    @Override
    protected RevisionProvider makeRevisionProvider(Path rootPath) {
        return new JGitRevisionProvider(rootPath);
    }
}
