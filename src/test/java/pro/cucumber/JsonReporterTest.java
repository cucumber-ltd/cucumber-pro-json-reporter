package pro.cucumber;

import cucumber.api.formatter.Formatter;
import cucumber.runtime.CucumberException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonReporterTest {

    @Ignore
    @Test
    public void it_publishes_the_json_report_with_filtered_environment_data() {
        Map<String, String> env = new HashMap<>();
        env.put("FOO", "bar");
        env.put("PASSWORD", "secret");
        CapturingPublisher publisher = new CapturingPublisher();
        Formatter reporter = new JsonReporter(
                publisher,
                env,
                JsonReporter.DEFAULT_ENV_MASK,
                JsonReporter.DEFAULT_CUCUMBER_PROFILE_NAME
        );

        // TODO: use reporter

        assertEquals("FOO=BAR\n", publisher.getPublishedEnv());

        // TODO: better check for file?
        assertNotNull(publisher.getPublishedFile());
    }

    class CapturingPublisher implements Publisher {

        private File file;
        private String env;

        @Override
        public void publish(File file, String env, String profileName) throws CucumberException {
            this.file = file;
            this.env = env;
        }

        public String getPublishedEnv() {
            return env;
        }

        public File getPublishedFile() {
            return file;
        }
    }

}