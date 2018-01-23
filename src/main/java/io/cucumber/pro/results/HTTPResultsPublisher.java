package io.cucumber.pro.results;

import cucumber.runtime.CucumberException;
import io.cucumber.pro.Env;
import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
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

class HTTPResultsPublisher implements ResultsPublisher {

    private static final String PART_ENV = "env";
    private static final String PART_PAYLOAD = "payload";
    private static final String PART_PROFILE_NAME = "profileName";
    private static final String CONTENT_TYPE_CUCUMBER_JAVA_RESULTS_JSON = "application/x.cucumber.java.results+json";
    private final String url;
    private final String authToken;
    private final Config config;
    private final Logger logger;

    /**
     * @param url    where to send results
     * @param logger where to print errors and warnings
     */
    HTTPResultsPublisher(String url, Config config, Logger logger) {
        this.url = url;
        this.config = config;
        authToken = config.get(Env.CUCUMBER_PRO_TOKEN, null);
        this.logger = logger;
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
                logger.log(Logger.Level.INFO, "Published results to Cucumber Pro: " + url);
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);
                String responseBody = new String(baos.toByteArray(), "UTF-8");

                String suggestion = "";
                if (statusCode == 401)
                    suggestion = String.format("You need to define %s", Env.CUCUMBER_PRO_TOKEN);
                if (statusCode == 403)
                    suggestion = String.format("You need to change the value of %s", Env.CUCUMBER_PRO_TOKEN);

                throw new CucumberException(String.format(
                        "Failed to publish results to Cucumber Pro URL: %s, Status: %s\n%s\n%s",
                        url,
                        statusLine,
                        responseBody,
                        suggestion
                ));
            }
        } catch (ConnectTimeoutException | HttpHostConnectException e) {
            if (config.getBoolean(Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR, true)) {
                logger.log(Logger.Level.WARN, "Failed to publish results to %s\n", url);
            } else {
                throw new CucumberException(String.format("Failed to publish results to %s\nYou can set %s to true to treat this as a warning instead of an error", url, Env.CUCUMBER_PRO_IGNORE_CONNECTION_ERROR), e);
            }
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private HttpClient buildHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        int timeout = config.getInt(Env.CUCUMBER_PRO_CONNECTION_TIMEOUT_MILLIS, 5000);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

        if (authToken != null) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(authToken, "");
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
