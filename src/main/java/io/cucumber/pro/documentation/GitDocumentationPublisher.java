package io.cucumber.pro.documentation;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.IdentityRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.agentproxy.AgentProxyException;
import com.jcraft.jsch.agentproxy.Connector;
import com.jcraft.jsch.agentproxy.ConnectorFactory;
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository;
import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;
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

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

public class GitDocumentationPublisher implements DocumentationPublisher {
    private final String remote;
    private final int port;
    private final String hostKey;
    private final Env env;
    private final Logger logger;

    GitDocumentationPublisher(String remote, int port, String hostKey, Env env, Logger logger) {
        this.remote = remote;
        this.port = port;
        this.hostKey = hostKey;
        this.env = env;
        this.logger = logger;
        if (env.getBoolean(Env.CUCUMBER_PRO_GIT_DEBUG, false)) {
            JSch.setLogger(new VerboseJschLogger());
        }
    }

    @Override
    public void publish() {
        try {
            this.publish0();
            logger.info("Published documentation to Cucumber Pro: " + remote);
        } catch (IOException e) {
            throw new RuntimeException("IO error", e);
        } catch (GitAPIException e) {
            if (env.getBoolean(Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, false)) {
                logger.warn("Failed to publish documentation to %s\n", remote);
            } else {
                throw new RuntimeException(String.format("Failed to publish documentation to %s\nYou can define %s=true to treat this as a warning instead of an error", remote, Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR), e);
            }
        } catch (JSchException e) {
            throw new RuntimeException("SSH error", e);
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
        if (env.getBoolean(Env.CUCUMBER_PRO_GIT_DEBUG, false)) {
            pushCommand.setProgressMonitor(new TextProgressMonitor());
        }
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
                session.setPort(port);
            }

            @Override
            protected JSch getJSch(OpenSshConfig.Host host, FS fs) throws JSchException {
                JSch jsch = super.createDefaultJSch(fs);
                if (hostKey != null) {
                    HostKey key = new HostKey(host.getHostName(), DatatypeConverter.parseBase64Binary(hostKey));
                    jsch.getHostKeyRepository().add(key, null);
                }
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
