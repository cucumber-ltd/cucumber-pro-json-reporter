package io.cucumber.pro.documentation;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Test;

public class GitDocumentationPublisherTest {
    @Test
    public void pushes_to_git() {
        GitDocumentationPublisher publisher = new GitDocumentationPublisher("git@git.cucumber.pro:cucumber-pro-plugin-jvm.git", System.getenv("IDENTITY_PASSPHRASE"));
        publisher.publish();
    }

    @Test
    public void test_ssh() throws JSchException {
        // This test is just for debugging SSH connection problems on Circle CI.
        // Once it passes it can probably be deleted.

        JSch jsch = new JSch();
        String authorizedKeys = System.getProperty("user.home") + "/.ssh/authorized_keys";
        String identity = System.getProperty("user.home") + "/.ssh/id_rsa";
        jsch.setKnownHosts(authorizedKeys);
        jsch.addIdentity(identity, System.getenv("IDENTITY_PASSPHRASE"));

        Session session = jsch.getSession("git", "git.cucumber.pro", 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(50000);
        Channel channel = session.openChannel("exec");
        channel.setOutputStream(System.out);
        channel.setInputStream(System.in);
        channel.connect(30000);
    }
}
