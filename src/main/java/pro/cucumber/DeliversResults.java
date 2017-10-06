package pro.cucumber;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

class DeliversResults {

    private final URI url;

    DeliversResults(URI url) {
        this.url = url;
    }

    void post(File jsonFile, final String env) throws IOException {
        HttpPost post = new HttpPost(url);
        FileBody fileBody = new FileBody(jsonFile, ContentType.create("application/x.cucumber.java.results+json", "UTF-8"));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("payload", fileBody);
        builder.addPart("env", new AbstractContentBody(ContentType.TEXT_PLAIN) {

            @Override
            public String getTransferEncoding() {
                return MIME.ENC_BINARY;
            }

            @Override
            public long getContentLength() {
                return env.length();
            }

            @Override
            public String getFilename() {
                return "env";
            }

            @Override
            public void writeTo(OutputStream out) throws IOException {
                out.write(env.getBytes("UTF-8"));
            }
        });
        HttpEntity entity = builder.build();
        post.setEntity(entity);

        HttpClient client = HttpClientBuilder.create().build();
        client.execute(post);
    }
}
