package pro.cucumber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class GitWorkingCopyTest {
    private Path rootPath;

    @Before
    public void createScm() throws IOException {
        rootPath = Files.createTempDirectory("GitWC");
        Path subfolder = rootPath.resolve("subfolder");
        Files.createDirectory(subfolder);
        Files.createFile(subfolder.resolve("file"));
        Exec.cmd("git init", rootPath);
        Exec.cmd("git add -A", rootPath);
        Exec.cmd("git commit -am \"files\"", rootPath);
    }

    @Test
    public void findsRev() {
        String sha1Pattern = "^[a-f0-9]{40}$";
        GitWorkingCopy workingCopy = new GitWorkingCopy(rootPath);

        assertTrue("Expected a sha1", Pattern.matches(sha1Pattern, workingCopy.getRev()));
    }
}