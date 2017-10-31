package io.cucumber.pro;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.Env;
import cucumber.runtime.formatter.PluginFactory;
import io.cucumber.pro.metadata.ChainedMetadata;
import io.cucumber.pro.metadata.Metadata;
import io.cucumber.pro.publisher.HTTPPublisher;
import io.cucumber.pro.publisher.Publisher;
import io.cucumber.pro.revision.RevisionProvider;
import io.cucumber.pro.revision.jgit.JGitRevisionProvider;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReporter implements Formatter {

    static final String DEFAULT_ENV_MASK = "SECRET|KEY|TOKEN|PASSWORD";
    static final String DEFAULT_CUCUMBER_PROFILE_NAME = "cucumber-jvm-unspecified-profile";
    private static final Env ENV = new Env();
    private final Formatter jsonFormatter;
    private final File jsonFile;
    private final FilteredEnv filteredEnv;
    private final Publisher publisher;
    private final String profileName;

    JsonReporter(Publisher publisher, Map<String, String> env, String envMask, String profileName) {
        this.publisher = publisher;
        this.profileName = profileName;
        try {
            jsonFile = File.createTempFile("cucumber-json", ".json");
        } catch (IOException e) {
            throw new CucumberException(e);
        }
        jsonFile.deleteOnExit();
        jsonFormatter = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());

        filteredEnv = new FilteredEnv(envMask, env);
    }

    public JsonReporter(String profileName) {
        this(createPublisher(), System.getenv(), ENV.get("CUCUMBER_PRO_ENV_MASK", DEFAULT_ENV_MASK), profileName);
    }

    public JsonReporter() {
        this(createPublisher(), System.getenv(), ENV.get("CUCUMBER_PRO_ENV_MASK", DEFAULT_ENV_MASK), ENV.get("CUCUMBER_PROFILE_NAME", DEFAULT_CUCUMBER_PROFILE_NAME));
    }

    private static Publisher createPublisher() {
        Metadata metadata = ChainedMetadata.create();
        String projectName = metadata.getProjectName();
        String revision = createRevisionProvider().getRevision();
        return HTTPPublisher.create(System.getenv(), projectName, revision);
    }


    private static RevisionProvider createRevisionProvider() {
        String revisionProviderClassName = ENV.get("CUCUMBER_PRO_REVISION_PROVIDER", JGitRevisionProvider.class.getName());
        try {
            Class<RevisionProvider> providerClass = (Class<RevisionProvider>) Thread.currentThread().getContextClassLoader().loadClass(revisionProviderClassName);
            return providerClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new CucumberException(e);
        }
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (jsonFormatter == null) return;
        jsonFormatter.setEventPublisher(new PostingEventPublisher(publisher));
    }

    private class PostingEventPublisher implements EventPublisher {
        private final EventPublisher publisher;

        PostingEventPublisher(EventPublisher publisher) {
            this.publisher = publisher;
        }

        @Override
        public <T extends Event> void registerHandlerFor(Class<T> eventType, EventHandler<T> handler) {
            publisher.registerHandlerFor(eventType, handler);

            if (eventType == TestRunFinished.class) {
                publisher.registerHandlerFor(TestRunFinished.class, new EventHandler<TestRunFinished>() {
                    @Override
                    public void receive(TestRunFinished event) {
                        JsonReporter.this.publisher.publish(jsonFile, filteredEnv.toString(), profileName);
                    }
                });
            }
        }
    }
}

