package io.cucumber.pro.revision;

import io.cucumber.pro.TestLogger;
import org.junit.Test;

import java.util.regex.Pattern;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GitRevisionProviderTest {
    @Test
    public void findsRevision() {
        String sha1Pattern = "^[a-f0-9]{40}$";
        RevisionProvider revisionProvider = new GitRevisionProvider(new TestLogger());
        String rev = revisionProvider.getRevision();
        assertTrue("Expected a sha1, got: " + rev, Pattern.matches(sha1Pattern, rev));
    }

    @Test
    public void findsBranch() {
        RevisionProvider revisionProvider = new GitRevisionProvider(new TestLogger());
        String branch = revisionProvider.getBranch();
        // Can't expect it to be master. During release it's a SHA!
        assertNotNull(branch);
    }
}
