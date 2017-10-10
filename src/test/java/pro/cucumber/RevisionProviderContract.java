package pro.cucumber;

import org.junit.Before;
import org.junit.Test;
import pro.cucumber.gitcli.GitCliRevisionProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public abstract class RevisionProviderContract {
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
        RevisionProvider revisionProvider = makeRevisionProvider(rootPath);
        String rev = revisionProvider.getRev();
        assertTrue("Expected a sha1, got: "+rev, Pattern.matches(sha1Pattern, rev));
    }

    protected abstract RevisionProvider makeRevisionProvider(Path rootPath);

}