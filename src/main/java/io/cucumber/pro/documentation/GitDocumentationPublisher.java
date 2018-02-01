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
import io.cucumber.pro.Keys;
import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;

public class GitDocumentationPublisher implements DocumentationPublisher {
    private final RemoteSpec pushSpec;
    private final Logger logger;
    private final boolean ignoreConnectionError;
    private final String fetchRemoteName;
    private final boolean fetchFromSource;

    GitDocumentationPublisher(RemoteSpec pushSpec, Config config, Logger logger) {
        this.pushSpec = pushSpec;
        this.logger = logger;
        JSch.setLogger(new JschLogger(logger));
        ignoreConnectionError = config.getBoolean(Keys.CUCUMBERPRO_CONNECTION_IGNOREERROR);
        fetchRemoteName = config.getString(Keys.CUCUMBERPRO_GIT_SOURCE_REMOTE);
        fetchFromSource = config.getBoolean(Keys.CUCUMBERPRO_GIT_SOURCE_FETCH);
    }

    private static <C extends GitCommand, R> void configureSsh(RemoteSpec remoteSpec, TransportCommand<C, R> push, Logger logger) throws JSchException {
        final SshSessionFactory sshSessionFactory = getSshSessionFactory(remoteSpec, logger);
        push.setTransportConfigCallback(new TransportConfigCallback() {
            @Override
            public void configure(Transport transport) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        });
    }

    static Git getGit() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .readEnvironment()
                .findGitDir()
                .setMustExist(true)
                .build();
        return new Git(repository);
    }

    private static SshSessionFactory getSshSessionFactory(final RemoteSpec remoteSpec, final Logger logger) throws JSchException {
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
                jsch.setIdentityRepository(getIdentityRepository(logger));
                return jsch;
            }
        };
    }

    private static IdentityRepository getIdentityRepository(Logger logger) throws JSchException {
        try {
            ConnectorFactory connectorFactory = ConnectorFactory.getDefault();
            Connector connector = connectorFactory.createConnector();
            return new RemoteIdentityRepository(connector);
        } catch (AgentProxyException e) {
            throw logger.log(e, "Failed to configure SSH Agent");
        }
    }

    @Override
    public void publish() {
        try {
            Git git = getGit();
            if (fetchFromSource) {
                fetch(git, fetchRemoteName);
            }
            push(git);
        } catch (IOException e) {
            throw logger.log(e, "IO error");
        }
    }

    void fetch(Git git, String fetchRemoteName) throws IOException {
        String fetchRemote = git.getRepository().getConfig().getString("remote", fetchRemoteName, "url");
        RemoteSpec fetchSpec = new RemoteSpec(
                fetchRemote,
                22,
                null
        );

        try {
            this.fetch0(git, fetchSpec, fetchRemoteName);
            logger.log(Logger.Level.INFO, "Fetched all commits from " + fetchSpec.remote);
        } catch (GitAPIException e) {
            if (ignoreConnectionError) {
                logger.log(Logger.Level.INFO, "Failed to fetch commits from %s\n", fetchSpec.remote);
            } else {
                throw logger.log(e, String.format("Failed to fetch commits from %s\nYou can set %s to true to treat this as a warning instead of an error", fetchSpec.remote, Keys.CUCUMBERPRO_CONNECTION_IGNOREERROR));
            }
        } catch (JSchException e) {
            throw logger.log(e, "SSH error");
        }
    }

    private void push(Git git) throws IOException {
        try {
            this.push0(git);
            logger.log(Logger.Level.INFO, "Published documentation to Cucumber Pro: " + pushSpec.remote);
        } catch (GitAPIException e) {
            if (ignoreConnectionError) {
                logger.log(Logger.Level.WARN, "Failed to publish documentation to %s\n", pushSpec.remote);
            } else {
                throw logger.log(e, String.format("Failed to publish documentation to %s\nYou can set %s to true to treat this as a warning instead of an error", pushSpec.remote, Keys.CUCUMBERPRO_CONNECTION_IGNOREERROR));
            }
        } catch (JSchException e) {
            throw logger.log(e, "SSH error");
        }
    }

    private void fetch0(Git git, RemoteSpec fetchSpec, String fetchRemoteName) throws JSchException, GitAPIException {
        FetchCommand fetch = git.fetch().setCheckFetchedObjects(true);
        fetch.setProgressMonitor(new TextProgressMonitor());
        fetch.setRemote(fetchSpec.remote);

        StoredConfig config = git.getRepository().getConfig();
        List<RefSpec> refSpecs = config.getRefSpecs("remote", fetchRemoteName, "fetch");
        fetch.setRefSpecs(refSpecs);
        configureSsh(fetchSpec, fetch, logger);

        FetchResult result = fetch.call();
        logResult(result);
    }

    private void push0(Git git) throws IOException, GitAPIException, JSchException {
        PushCommand push = git.push();
        push.setProgressMonitor(new TextProgressMonitor());
        push.setRemote(pushSpec.remote);
        push.setForce(true);
        configureSsh(pushSpec, push, logger);

        Iterable<PushResult> result = push.call();
        for (PushResult pushResult : result) {
            logResult(pushResult);
        }
    }

    private void logResult(OperationResult result) {
        logger.log(Logger.Level.INFO, result.getMessages());
    }

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
}
