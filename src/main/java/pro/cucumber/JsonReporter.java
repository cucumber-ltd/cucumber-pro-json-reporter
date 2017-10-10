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
    private static final String CUCUMBER_PRO_URL = System.getenv("CUCUMBER_PRO_URL");

    private final Formatter f;
    private final File jsonFile;
    private final FilteredEnv filteredEnv;
    private final DeliversResults deliversResults;

    public static URI createResultsUri(String basePath, String revision) throws URISyntaxException {
        if (!basePath.endsWith("/"))
            basePath = basePath + "/";
        return new URI(basePath + revision);
    }

    public JsonReporter() throws IOException, URISyntaxException {
        if (CUCUMBER_PRO_URL != null) {
            jsonFile = File.createTempFile("cucumber-json", ".json");
            jsonFile.deleteOnExit();
            f = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());

            filteredEnv = new FilteredEnv(System.getenv("CUCUMBER_PRO_ENV_MASK"), System.getenv());

            GitWorkingCopy workingCopy = GitWorkingCopy.detect(Paths.get(System.getProperty("user.dir")));
            String rev = workingCopy.getRev();

            URI url = JsonReporter.createResultsUri(CUCUMBER_PRO_URL, rev);
            deliversResults = new DeliversResults(url);
        } else {
            System.err.println("CUCUMBER_PRO_URL not defined. Cannot send results to Cucumber Pro.");
            f = null;
            jsonFile = null;
            filteredEnv = null;
            deliversResults = null;
        }
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (f == null) return;
        f.setEventPublisher(new PostingEventPublisher(publisher));
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
                        try {
                            deliversResults.post(jsonFile, filteredEnv.toString());
                        } catch (IOException e) {
                            throw new CucumberException(e);
                        }
                    }
                });
            }
        }
    }
}

