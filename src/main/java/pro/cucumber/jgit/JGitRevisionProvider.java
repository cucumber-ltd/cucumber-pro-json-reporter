package pro.cucumber.jgit;

import cucumber.runtime.CucumberException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import pro.cucumber.RevisionProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JGitRevisionProvider implements RevisionProvider {
    private final Repository repository;

    public JGitRevisionProvider() {
        this(Paths.get(System.getProperty("user.dir")));
    }

    public JGitRevisionProvider(Path rootPath) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        // scan environment GIT_* variables
        // scan up the file system tree
        try {
            repository = builder
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir(rootPath.toFile()) // scan up the file system tree
                    .setMustExist(true)
                    .build();
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    @Override
    public String getRev() {
        try {
            return repository.exactRef("HEAD").getObjectId().getName();
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }
}
