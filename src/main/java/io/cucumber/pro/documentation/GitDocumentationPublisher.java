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
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

public class GitDocumentationPublisher implements DocumentationPublisher {
    private final RemoteSpec pushSpec;
    private final RemoteSpec fetchSpec;
    private final Logger logger;
    private final boolean gitDebug;
    private final boolean ignoreConnectionError;

    static class RemoteSpec {
        private final String remote;
        private final int port;
        private final String hostKey;

        RemoteSpec(String remote, int port, String hostKey) {
            this.remote = remote;
            this.port = port;
            this.hostKey = hostKey;
        }
    }

    GitDocumentationPublisher(RemoteSpec pushSpec, RemoteSpec fetchSpec, Env env, Logger logger) {
        this.pushSpec = pushSpec;
        this.fetchSpec = fetchSpec;
        this.logger = logger;
        if (env.getBoolean(Env.CUCUMBER_PRO_GIT_DEBUG, false)) {
            JSch.setLogger(new VerboseJschLogger());
        }
        gitDebug = env.getBoolean(Env.CUCUMBER_PRO_GIT_DEBUG, false);
        ignoreConnectionError = env.getBoolean(Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, false);
    }

    @Override
    public void publish() {
        try {
            Git git = getGit();
            if(fetchSpec != null )
                fetch(git);
            push(git);
        } catch (IOException e) {
            throw new RuntimeException("IO error", e);
        }
    }

    private void fetch(Git git) throws IOException {
        try {
            this.fetch0(git);
            logger.info("Fetched all commits from " + fetchSpec.remote);
        } catch (GitAPIException e) {
            if (ignoreConnectionError) {
                logger.warn("Failed to fetch commits from %s\n", fetchSpec.remote);
            } else {
                throw new RuntimeException(String.format("Failed to fetch commits from %s\nYou can define %s=true to treat this as a warning instead of an error", fetchSpec.remote, Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR), e);
            }
        } catch (JSchException e) {
            throw new RuntimeException("SSH error", e);
        }
    }

    private void push(Git git) throws IOException {
        try {
            this.push0(git);
            logger.info("Published documentation to Cucumber Pro: " + pushSpec.remote);
        } catch (GitAPIException e) {
            if (ignoreConnectionError) {
                logger.warn("Failed to publish documentation to %s\n", pushSpec.remote);
            } else {
                throw new RuntimeException(String.format("Failed to publish documentation to %s\nYou can define %s=true to treat this as a warning instead of an error", pushSpec.remote, Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR), e);
            }
        } catch (JSchException e) {
            throw new RuntimeException("SSH error", e);
        }
    }

    private void fetch0(Git git) throws JSchException, GitAPIException {
        FetchCommand fetch = git.fetch();
        if (gitDebug) {
            fetch.setProgressMonitor(new TextProgressMonitor());
        }
        fetch.setRemote(fetchSpec.remote);
        configureSsh(pushSpec, fetch);

        FetchResult result = fetch.call();
        logResult(result);
    }

    private void push0(Git git) throws IOException, GitAPIException, JSchException {
        PushCommand push = git.push();
        if (gitDebug) {
            push.setProgressMonitor(new TextProgressMonitor());
        }
        push.setRemote(pushSpec.remote);
        configureSsh(pushSpec, push);

        Iterable<PushResult> result = push.call();
        for (PushResult pushResult : result) {
            logResult(pushResult);
        }
    }

    private <C extends GitCommand, R> void configureSsh(RemoteSpec pushSpec, TransportCommand<C, R> push) throws JSchException {
        final SshSessionFactory sshSessionFactory = getSshSessionFactory(pushSpec);
        push.setTransportConfigCallback(new TransportConfigCallback() {
            @Override
            public void configure(Transport transport) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        });
    }

    private void logResult(OperationResult result) {
        if (gitDebug) {
            logger.info(result.getMessages());
        }
    }

    private Git getGit() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .readEnvironment()
                .findGitDir()
                .setMustExist(true)
                .build();
        return new Git(repository);
    }

    private SshSessionFactory getSshSessionFactory(final RemoteSpec remoteSpec) throws JSchException {
        return new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setPort(remoteSpec.port);
            }

            @Override
            protected JSch getJSch(OpenSshConfig.Host host, FS fs) throws JSchException {
                JSch jsch = super.createDefaultJSch(fs);
                if (remoteSpec.hostKey != null) {
                    HostKey key = new HostKey(host.getHostName(), DatatypeConverter.parseBase64Binary(remoteSpec.hostKey));
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
