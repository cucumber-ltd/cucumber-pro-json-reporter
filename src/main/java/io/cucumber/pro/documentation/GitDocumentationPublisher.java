package io.cucumber.pro.documentation;

import com.jcraft.jsch.IdentityRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.agentproxy.AgentProxyException;
import com.jcraft.jsch.agentproxy.Connector;
import com.jcraft.jsch.agentproxy.ConnectorFactory;
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository;
import io.cucumber.pro.Env;
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

public class GitDocumentationPublisher implements DocumentationPublisher {
    public static final String ENV_CUCUMBER_PRO_GIT_DEBUG = "CUCUMBER_PRO_GIT_DEBUG";
    private final String remote;

    public GitDocumentationPublisher(String remote, Env env) {
        this.remote = remote;
        if (env.getBoolean(ENV_CUCUMBER_PRO_GIT_DEBUG, false)) {
            JSch.setLogger(new VerboseJschLogger());
        }
    }

    @Override
    public void publish() {
        try {
            this.publish0();
            System.out.println("Published documentation to Cucumber Pro: " + remote);
        } catch (IOException e) {
            throw new RuntimeException("IO error", e);
        } catch (GitAPIException e) {
            throw new RuntimeException("Git API error", e);
        } catch (JSchException e) {
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
                jsch.setIdentityRepository(getIdentityRepository());
                return jsch;
            }
        };
    }

    private IdentityRepository getIdentityRepository() throws JSchException {
        try {
            ConnectorFactory connectorFactory = ConnectorFactory.getDefault();
            Connector connector = connectorFactory.createConnector();
            return new RemoteIdentityRepository(connector);
        } catch (AgentProxyException e) {
            throw new JSchException("Failed to configure SSH Agent", e);
        }
    }
}
