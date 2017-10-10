package pro.cucumber;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.Env;
import cucumber.runtime.formatter.PluginFactory;
import pro.cucumber.gitcli.GitCliRevisionProvider;
import pro.cucumber.jgit.JGitRevisionProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JsonReporter implements Formatter {

    public static final String DEFAULT_ENV_MASK = "SECRET|KEY|TOKEN|PASSWORD";
    private final Formatter jsonFormatter;
    private final File jsonFile;
    private final FilteredEnv filteredEnv;
    private final Publisher publisher;
    private static final Env ENV = new Env();

    public JsonReporter(Publisher publisher) throws IOException, URISyntaxException {
        this.publisher = publisher;
        jsonFile = File.createTempFile("cucumber-json", ".json");
        jsonFile.deleteOnExit();
        jsonFormatter = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());

        filteredEnv = new FilteredEnv(ENV.get("CUCUMBER_PRO_ENV_MASK", DEFAULT_ENV_MASK), System.getenv());
    }

    public JsonReporter() throws IOException, URISyntaxException {
        this(createPublisher());
    }

    private static Publisher createPublisher() {
        return new HTTPPublisher(createRevisionProvider());
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
                        JsonReporter.this.publisher.publish(jsonFile, filteredEnv.toString());
                    }
                });
            }
        }
    }
}

