package pro.cucumber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class GitWorkingCopyTest {
    private Path rootPath;
    private Path subfolder;

    @Before
    public void createScm() throws IOException {
        // TODO
        rootPath = Files.createTempDirectory("GitWC");
        subfolder = rootPath.resolve("subfolder");
        Files.createDirectory(subfolder);
        Files.createFile(subfolder.resolve("file"));
        Exec.cmd("git init", rootPath);
        Exec.cmd("git add -A", rootPath);
        Exec.cmd("git commit -am \"files\"", rootPath);
    }

    @After
    public void cleanup() throws IOException {
        if (Files.exists(rootPath))
        {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
                {
                    Files.delete(path);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException
                {
                    Files.delete(directory);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Test
    public void detectsWcFromWcSubDir() {
        GitWorkingCopy workingCopy = GitWorkingCopy.detect(subfolder);
        assertEquals(rootPath, workingCopy.getRootPath());
    }

    @Test
    public void detectsWcFromWcRootDir() {
        GitWorkingCopy workingCopy = GitWorkingCopy.detect(rootPath);
        assertEquals(rootPath, workingCopy.getRootPath());
    }

    @Test
    public void doesNotDetectWcOutsideWcDir() {
        GitWorkingCopy workingCopy = GitWorkingCopy.detect(rootPath.getParent());
        assertNull(workingCopy);
    }

    @Test
    public void findsRev() {
        String sha1Pattern = "^[a-f0-9]{40}$";
        GitWorkingCopy workingCopy = GitWorkingCopy.detect(rootPath);

        assertTrue("Expected a sha1", Pattern.matches(sha1Pattern, workingCopy.getRev()));
    }
}