package io.cucumber.pro.revision.jgit;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.revision.RevisionProvider;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JGitRevisionProvider implements RevisionProvider {
    private final Repository repository;

    public JGitRevisionProvider() {
        this(Paths.get(System.getProperty("user.dir")));
    }

    JGitRevisionProvider(Path rootPath) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder
                    .readEnvironment()
                    .findGitDir(rootPath.toFile())
                    .setMustExist(true)
                    .build();
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    @Override
    public String getRevision() {
        try {
            return repository.exactRef("HEAD").getObjectId().getName();
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }
}
