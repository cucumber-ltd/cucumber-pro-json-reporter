package io.cucumber.pro.revision.git;

import io.cucumber.pro.revision.RevisionProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Disabled by default, only in the codebase as a fallback in case JGit
 * doesn't work well. Delete this when/if jgit proves to be stable
 */
public class GitRevisionProvider implements RevisionProvider {

    private final Path rootPath;
    private final Exec exec;

    public GitRevisionProvider() {
        this(Paths.get(System.getProperty("user.dir")));
    }

    public GitRevisionProvider(Path rootPath) {
        this.rootPath = rootPath;
        exec = new Exec(rootPath);
    }

    public static GitRevisionProvider detect(Path directory) {
        Path workingCopyRoot = null;
        while (directory != null) {
            Path dotGit = directory.resolve(".git");
            if (Files.isDirectory(dotGit)) {
                workingCopyRoot = directory;
            }
            directory = directory.getParent();
        }
        return workingCopyRoot != null ? new GitRevisionProvider(workingCopyRoot) : null;
    }

    public Path getRootPath() {
        return this.rootPath;
    }

    @Override
    public String getRev() {
        return exec.cmd("git rev-parse HEAD").get(0);
    }
}
