package pro.cucumber.gitcli;

import pro.cucumber.Exec;
import pro.cucumber.RevisionProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GitCliRevisionProvider implements RevisionProvider {

    private final Path rootPath;
    private final Exec exec;

    public GitCliRevisionProvider() {
        this(Paths.get(System.getProperty("user.dir")));
    }

    public GitCliRevisionProvider(Path rootPath) {
        this.rootPath = rootPath;
        exec = new Exec(rootPath);
    }

    public static GitCliRevisionProvider detect(Path directory) {
        Path workingCopyRoot = null;
        while (directory != null) {
            Path dotGit = directory.resolve(".git");
            if (Files.isDirectory(dotGit)) {
                workingCopyRoot = directory;
            }
            directory = directory.getParent();
        }
        return workingCopyRoot != null ? new GitCliRevisionProvider(workingCopyRoot) : null;
    }

    public Path getRootPath() {
        return this.rootPath;
    }

    @Override
    public String getRev() {
        return exec.cmd("git rev-parse HEAD").get(0);
    }
}
