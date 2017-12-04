package io.cucumber.pro.revision;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Logger;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitRevisionProvider implements RevisionProvider {
    private final Repository repository;

    public GitRevisionProvider(Logger logger) {
        File currentDirectory = new File(System.getProperty("user.dir"));
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            builder.readEnvironment()
                    .findGitDir(currentDirectory)
                    .setMustExist(true);
            if (builder.getGitDir() == null) {
                throw new CucumberException(String.format("The current directory '%s' and none of its parent directories appear to have a '.git' directory", currentDirectory));
            } else {
                logger.log(Logger.Level.INFO, "Current directory: '%s', Git directory: '%s'", currentDirectory, builder.getGitDir().getAbsolutePath());
            }
            repository = builder.build();
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
