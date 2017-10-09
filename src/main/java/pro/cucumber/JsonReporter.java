package pro.cucumber;

import cucumber.api.event.EventPublisher;
import cucumber.api.formatter.Formatter;
import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.TestRunFinished;
import cucumber.runtime.CucumberException;
import cucumber.runtime.formatter.PluginFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class JsonReporter implements Formatter {

    private final Formatter f;
    private final File jsonFile;
    private final FilteredEnv filteredEnv;
    private DeliversResults deliversResults;

    public static URI createResultsUri(String basePath, String revision) throws URISyntaxException {
        if (!basePath.endsWith("/"))
            basePath = basePath + "/";
        return new URI(basePath + revision);
    }

    public JsonReporter() throws IOException, URISyntaxException {
        jsonFile = File.createTempFile("cucumber-json", ".json");
        jsonFile.deleteOnExit();
        f = (Formatter) new PluginFactory().create("json:" + jsonFile.getAbsolutePath());

        filteredEnv = new FilteredEnv(System.getenv("CUCUMBER_PRO_ENV_MASK"), System.getenv());

        GitWorkingCopy workingCopy = GitWorkingCopy.detect(Paths.get(System.getProperty("user.dir")));
        String rev = workingCopy.getRev();
        URI url = JsonReporter.createResultsUri(System.getenv("CUCUMBER_PRO_URL"), rev);
        deliversResults = new DeliversResults(url);
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
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

