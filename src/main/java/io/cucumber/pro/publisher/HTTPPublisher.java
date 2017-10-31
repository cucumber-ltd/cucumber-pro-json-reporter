package io.cucumber.pro.publisher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

import static io.cucumber.pro.metadata.EnvMetadata.CUCUMBER_PRO_PROJECT_NAME;
import static io.cucumber.pro.metadata.YamlMetadata.PROJECT_NAME_FIELD;
import static io.cucumber.pro.metadata.YamlMetadata.YAML_FILE_NAME;

public class HTTPPublisher implements Publisher {

    public static final String ENV_CUCUMBER_PRO_TOKEN = "CUCUMBER_PRO_TOKEN";
    public static final String PART_ENV = "env";
    public static final String PART_PAYLOAD = "payload";
    public static final String PART_PROFILE_NAME = "profileName";
    public static final String CONTENT_TYPE_CUCUMBER_JAVA_RESULTS_JSON = "application/x.cucumber.java.results+json";

    public static Publisher create(Map<String, String> env, final String projectName, final String revision) {
        if (projectName == null) {
            String message = String.format("Project name missing. Either define an environment variable called %s or create %s with key %s", CUCUMBER_PRO_PROJECT_NAME, YAML_FILE_NAME, PROJECT_NAME_FIELD);
            return new NullPublisher(message);
        }
        String username = env.get(ENV_CUCUMBER_PRO_TOKEN);
        String password = "";
        return new HTTPPublisher(CucumberProUrlBuilder.buildCucumberProUrl(env, projectName, revision), username, password);
    }

    private final String url;
    private final String username;
    private final String password;

    /**
     * @param url where to send results
     * @param username the auth token
     * @param password blank for now
     */
    public HTTPPublisher(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void publish(File resultsJsonFile, final String envString, String profileName) {
        HttpClient client = buildHttpClient();

        HttpPost post = new HttpPost(URI.create(url));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        try {
            builder.addPart(PART_ENV, new MemoryFileBody("env.txt", envString, ContentType.TEXT_PLAIN));
            builder.addPart(PART_PAYLOAD, new FileBody(resultsJsonFile, ContentType.create(CONTENT_TYPE_CUCUMBER_JAVA_RESULTS_JSON, "UTF-8")));
            builder.addPart(PART_PROFILE_NAME, new StringBody(profileName, ContentType.TEXT_PLAIN));
            HttpEntity entity = builder.build();
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode >= 200 && statusCode < 400) {
                System.out.println("Published results to Cucumber Pro: " + url);
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);
                String responseBody = new String(baos.toByteArray(), "UTF-8");
                throw new RuntimeException(String.format(
                        "Failed to publish results to Cucumber Pro URL: %s, Status: %s\nResponse:\n%s",
                        url,
                        statusLine,
                        responseBody
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpClient buildHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if(username != null) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(AuthScope.ANY, credentials);
            httpClientBuilder.setDefaultCredentialsProvider(provider);
        }
        return httpClientBuilder.build();
    }

    private class MemoryFileBody extends AbstractContentBody {
        private final String filename;
        private final byte[] data;

        MemoryFileBody(String filename, String data, ContentType contentType) throws UnsupportedEncodingException {
            super(contentType);
            this.filename = filename;
            this.data = data.getBytes("UTF-8");
        }

        @Override
        public String getFilename() {
            return filename;
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            outputStream.write(data);
        }

        @Override
        public String getTransferEncoding() {
            return "UTF-8";
        }

        @Override
        public long getContentLength() {
            return data.length;
        }
    }
}
