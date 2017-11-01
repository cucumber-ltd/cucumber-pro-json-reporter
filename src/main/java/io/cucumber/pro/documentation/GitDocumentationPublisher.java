package io.cucumber.pro.documentation;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

import java.io.IOException;
import java.util.Vector;

public class GitDocumentationPublisher implements DocumentationPublisher {
    private final String remote;
    private final String passphrase;

    static {
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

    public GitDocumentationPublisher(String remote, String passphrase) {
        this.remote = remote;
        this.passphrase = passphrase;
    }

    @Override
    public void publish() {
        try {
            this.publish0();
        } catch (IOException e) {
            throw new RuntimeException("IO error", e);
        } catch (GitAPIException e) {
            throw new RuntimeException("Git API error", e);
        } catch (JSchException e) {
            if(e.getCause() != null) {
                System.err.println("CAUSE");
                e.getCause().printStackTrace();
            }
            System.err.println("ERROR");
            e.printStackTrace();
            throw new RuntimeException("SSH error");
        }
    }

    private void publish0() throws IOException, GitAPIException, JSchException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .readEnvironment()
                .findGitDir()
                .setMustExist(true)
                .build();
        Git git = new Git(repository);
        PushCommand pushCommand = git.push();
        pushCommand.setProgressMonitor(new TextProgressMonitor());
        pushCommand.setRemote(remote);

        final SshSessionFactory sshSessionFactory = getSshSessionFactory();
        pushCommand.setTransportConfigCallback(new TransportConfigCallback() {
            @Override
            public void configure(Transport transport) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        });

        Iterable<PushResult> result = pushCommand.call();
        for (PushResult pushResult : result) {
            System.out.println(pushResult.getMessages());
        }
    }

    private SshSessionFactory getSshSessionFactory() throws JSchException {
        return new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                System.out.println("host = " + host);
            }

            @Override
            protected JSch getJSch(OpenSshConfig.Host host, FS fs) throws JSchException {
                JSch jsch = super.createDefaultJSch(fs);

                Vector identityNames = jsch.getIdentityNames();
                String privkey = (String) identityNames.get(0);
                if (passphrase != null) {
                    jsch.addIdentity(privkey, passphrase);
                } else {
                    jsch.addIdentity(privkey);
                }

                testShell(jsch);

                System.out.println("************* identityNames = " + identityNames);
                return jsch;
            }
        };
    }

    private void testShell(JSch jsch) {
        try {
            System.out.println("============================ SHELL");
            Session session = jsch.getSession("git", "git.cucumber.pro", 22);
            System.out.println("============================ A");
            session.connect(50000);
            System.out.println("============================ B");
            Channel channel = session.openChannel("shell");
            System.out.println("============================ C");
            channel.setOutputStream(System.out);
            System.out.println("============================ D");
            channel.setInputStream(System.in);
            System.out.println("============================ E");
            channel.connect(30000);
            System.out.println("============================ OK");
        } catch (JSchException e) {
            System.out.println("============================ ERROR");
            e.printStackTrace();
        }
        System.out.println("============================ DONE");
    }
}
