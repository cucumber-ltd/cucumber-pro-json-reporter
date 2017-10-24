package pro.cucumber;

import cucumber.runtime.CucumberException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

class HTTPPublisher implements Publisher {

    private static final String CUCUMBER_PRO_URL = System.getenv("CUCUMBER_PRO_URL");
    private final RevisionProvider revisionProvider;

    HTTPPublisher(RevisionProvider revisionProvider) {
        this.revisionProvider = revisionProvider;
    }

    static URI createResultsUri(String basePath, String revision) {
        if (!basePath.endsWith("/"))
            basePath = basePath + "/";
        try {
            return new URI(basePath + revision);
        } catch (URISyntaxException e) {
            throw new CucumberException(e);
        }
    }

    @Override
    public void publish(File file, final String env, String profileName) {
        if (CUCUMBER_PRO_URL == null) {
            System.err.println("CUCUMBER_PRO_URL not defined. Not sending results to Cucumber Pro.");
            return;
        }
        URI url = createResultsUri(CUCUMBER_PRO_URL, revisionProvider.getRev());
        HttpPost post = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


        HttpClient client = HttpClientBuilder.create().build();
        try {
            builder.addPart("env", new MemoryFileBody("env.txt", env, ContentType.TEXT_PLAIN));
            builder.addPart("payload", new FileBody(file, ContentType.create("application/x.cucumber.java.results+json", "UTF-8")));
            builder.addPart("profileName", new StringBody(profileName, ContentType.TEXT_PLAIN));
            HttpEntity entity = builder.build();
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode >= 200 && statusCode < 400) {
                System.out.println("Published results to Cucumber Pro: " + url.toString());
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);
                String responseBody = new String(baos.toByteArray(), "UTF-8");
                throw new CucumberException(String.format(
                        "Failed to publish results to Cucumber Pro URL: %s, Status: %s\nResponse:\n%s",
                        url,
                        statusLine,
                        responseBody
                ));
            }
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private class MemoryFileBody extends AbstractContentBody {
        private final String filename;
        private final byte[] data;

        public MemoryFileBody(String filename, String data, ContentType contentType) throws UnsupportedEncodingException {
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
