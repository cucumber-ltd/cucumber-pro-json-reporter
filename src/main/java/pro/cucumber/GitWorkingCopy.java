package pro.cucumber;

import java.nio.file.Files;
import java.nio.file.Path;

public class GitWorkingCopy implements RevisionProvider {

    private final Path rootPath;
    private final Exec exec;

    public GitWorkingCopy(Path rootPath) {
        this.rootPath = rootPath;
        exec = new Exec(rootPath);
    }

    public static GitWorkingCopy detect(Path directory) {
        Path workingCopyRoot = null;
        while (directory != null) {
            Path dotGit = directory.resolve(".git");
            if (Files.isDirectory(dotGit)) {
                workingCopyRoot = directory;
            }
            directory = directory.getParent();
        }
        return workingCopyRoot != null ? new GitWorkingCopy(workingCopyRoot) : null;
    }

    public Path getRootPath() {
        return this.rootPath;
    }

    @Override
    public String getRev() {
        return exec.cmd("git rev-parse HEAD").get(0);
    }
}
