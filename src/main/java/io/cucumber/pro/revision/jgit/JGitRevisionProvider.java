package io.cucumber.pro.revision.jgit;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.revision.RevisionProvider;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;

public class JGitRevisionProvider implements RevisionProvider {
    private final Repository repository;

    JGitRevisionProvider() {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder
                    .readEnvironment()
                    .findGitDir()
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
