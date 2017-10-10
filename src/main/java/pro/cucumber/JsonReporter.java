package pro.cucumber;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.formatter.PluginFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class JsonReporter implements Formatter {

    private final Formatter jsonFormatter;
    private final File jsonFile;
    private final FilteredEnv filteredEnv;
    private final Publisher publisher;

    public JsonReporter(Publisher publisher) throws IOException, URISyntaxException {
        this.publisher = publisher;
        jsonFile = File.createTempFile("cucumber-json", ".json");
        jsonFile.deleteOnExit();
        jsonFormatter = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());

        filteredEnv = new FilteredEnv(System.getenv("CUCUMBER_PRO_ENV_MASK"), System.getenv());
    }

    public JsonReporter() throws IOException, URISyntaxException {
        this(new HTTPPublisher(GitWorkingCopy.detect(Paths.get(System.getProperty("user.dir")))));
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

