package io.cucumber.pro.documentation;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class GitDocumentationPublisherTest {
//    @Test
//    public void pushes_to_git() {
//        GitDocumentationPublisher publisher = new GitDocumentationPublisher("git@git.cucumber.pro:cucumber-pro-plugin-jvm.git", System.getenv("IDENTITY_PASSPHRASE"));
//        publisher.publish();
//    }

    @Before
    public void enableLogging() {
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int i, String s) {
                System.out.format("%d: %s\n", i, s);
            }
        });
    }

    @After
    public void disableLogging() {
        JSch.setLogger(null);
    }

    @Test
    public void test_ssh() throws JSchException {
        // This test is just for debugging SSH connection problems on Circle CI.
        // Once it passes it can probably be deleted.

        JSch jsch = new JSch();
        File authorizedKeys = new File(System.getProperty("user.home") + "/.ssh/authorized_keys");
        File identity = new File(System.getProperty("user.home") + "/.ssh/id_rsa");

        System.out.println("IDENTITY " + identity.getAbsolutePath());
        System.out.println("F = " + identity.isFile());
        System.out.println("E = " + identity.canExecute());
        System.out.println("R = " + identity.canRead());
        System.out.println("W = " + identity.canWrite());

        System.out.println("IDENTITY DIR");
        System.out.println("F = " + identity.getParentFile().isDirectory());
        System.out.println("E = " + identity.getParentFile().canExecute());
        System.out.println("R = " + identity.getParentFile().canRead());
        System.out.println("W = " + identity.getParentFile().canWrite());

        jsch.setKnownHosts(authorizedKeys.getAbsolutePath());
        jsch.addIdentity(identity.getAbsolutePath(), System.getenv("IDENTITY_PASSPHRASE"));

        Session session = jsch.getSession("git", "git.cucumber.pro", 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(50000);
        Channel channel = session.openChannel("exec");
        channel.setOutputStream(System.out);
        channel.setInputStream(System.in);
        channel.connect(30000);
    }
}
