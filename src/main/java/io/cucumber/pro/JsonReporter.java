package io.cucumber.pro;

import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.formatter.PluginFactory;
import io.cucumber.pro.publisher.Publisher;
import io.cucumber.pro.publisher.PublisherFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReporter implements Formatter {

    static final String DEFAULT_ENV_MASK = "SECRET|KEY|TOKEN|PASSWORD";
    static final String DEFAULT_CUCUMBER_PROFILE_NAME = "cucumber-jvm-unspecified-profile";
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
        this(PublisherFactory.create(), System.getenv(), getenv("CUCUMBER_PRO_ENV_MASK", DEFAULT_ENV_MASK), profileName);
    }

    public JsonReporter() {
        this(PublisherFactory.create(), System.getenv(), getenv("CUCUMBER_PRO_ENV_MASK", DEFAULT_ENV_MASK), getenv("CUCUMBER_PROFILE_NAME", DEFAULT_CUCUMBER_PROFILE_NAME));
    }

    private static String getenv(String key, String defaultValue) {
        String value = System.getenv(key);
        if(value != null) return value;
        return defaultValue;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (jsonFormatter == null) return;
        jsonFormatter.setEventPublisher(new PublisherAdapter(publisher));
    }

    private class PublisherAdapter implements EventPublisher {
        private final EventPublisher publisher;

        PublisherAdapter(EventPublisher publisher) {
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

