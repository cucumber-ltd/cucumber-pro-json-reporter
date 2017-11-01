package io.cucumber.pro.documentation;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ConfigRepository;
import com.jcraft.jsch.IdentityRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.OpenSSHConfig;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.agentproxy.AgentProxyException;
import com.jcraft.jsch.agentproxy.Connector;
import com.jcraft.jsch.agentproxy.ConnectorFactory;
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
    public void test_ssh() throws JSchException, IOException, AgentProxyException {
        // This test is just for debugging SSH connection problems on Circle CI.
        // Once it passes it can probably be deleted.

        JSch jsch = new JSch();

        File authorizedKeys = new File(System.getProperty("user.home") + "/.ssh/authorized_keys");
        jsch.setKnownHosts(authorizedKeys.getAbsolutePath());

        File sshConfig = new File(System.getProperty("user.home") + "/.ssh/config");
        if (sshConfig.isFile()) {
            ConfigRepository configRepository = OpenSSHConfig.parseFile(sshConfig.getAbsolutePath());
            jsch.setConfigRepository(configRepository);
        }

        // TODO: Find all private keys in the usual place and add them too
        File identity = new File(System.getProperty("user.home") + "/.ssh/id_rsa");
        jsch.addIdentity(identity.getAbsolutePath(), System.getenv("IDENTITY_PASSPHRASE"));

        ConnectorFactory cf = ConnectorFactory.getDefault();
        Connector connector = cf.createConnector();

        IdentityRepository identityRepository = new RemoteIdentityRepository(connector);

        jsch.setIdentityRepository(identityRepository);

        Session session = jsch.getSession("git", "git.cucumber.pro", 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(50000);
        Channel channel = session.openChannel("exec");
        channel.setOutputStream(System.out);
        channel.setInputStream(System.in);
        channel.connect(30000);
    }
}
