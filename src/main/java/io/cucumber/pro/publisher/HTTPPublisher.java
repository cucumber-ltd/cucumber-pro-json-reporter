package io.cucumber.pro.publisher;

import io.cucumber.pro.revision.RevisionProvider;
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

public class HTTPPublisher implements Publisher {

    private final String baseUri;
    private final RevisionProvider revisionProvider;

    public HTTPPublisher(String baseUri, RevisionProvider revisionProvider) {
        this.baseUri = baseUri;
        this.revisionProvider = revisionProvider;
    }

    @Override
    public void publish(File file, final String env, String profileName) {
        if(baseUri == null) return;
        URI uri = getUri();
        HttpPost post = new HttpPost(uri);

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
                System.out.println("Published results to Cucumber Pro: " + uri.toString());
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);
                String responseBody = new String(baos.toByteArray(), "UTF-8");
                throw new RuntimeException(String.format(
                        "Failed to publish results to Cucumber Pro URL: %s, Status: %s\nResponse:\n%s",
                        uri,
                        statusLine,
                        responseBody
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    URI getUri() {
        URI uri = URI.create(baseUri + (baseUri.endsWith("/") ? "" : "/"));
        return uri.resolve(revisionProvider.getRev());
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
