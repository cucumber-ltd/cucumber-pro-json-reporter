package pro.cucumber;

import cucumber.runtime.CucumberException;
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
import java.net.URISyntaxException;

public class HTTPPublisher implements Publisher {

    static final String CUCUMBER_PRO_URL = System.getenv("CUCUMBER_PRO_URL");
    private final RevisionProvider revisionProvider;

    HTTPPublisher(RevisionProvider revisionProvider) {
        this.revisionProvider = revisionProvider;
    }

    public static URI createResultsUri(String basePath, String revision) {
        if (!basePath.endsWith("/"))
            basePath = basePath + "/";
        try {
            return new URI(basePath + revision);
        } catch (URISyntaxException e) {
            throw new CucumberException(e);
        }
    }

    @Override
    public void publish(File file, final String env) {
        if (CUCUMBER_PRO_URL == null) {
            System.err.println("CUCUMBER_PRO_URL not defined. Cannot send results to Cucumber Pro.");
            return;
        }
        URI url = createResultsUri(CUCUMBER_PRO_URL, revisionProvider.getRev());
        HttpPost post = new HttpPost(url);
        FileBody fileBody = new FileBody(file, ContentType.create("application/x.cucumber.java.results+json", "UTF-8"));

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
        try {
            client.execute(post);
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }
}
