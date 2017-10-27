package io.cucumber.proreporter;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class HTTPPublisherTest {
    @Test
    public void addRevisionToUrl() throws MalformedURLException, URISyntaxException {
        String urlString = "https://example.com/tests/results";
        String revision = "abcd1234";
        URI url = HTTPPublisher.createResultsUri(urlString, revision);
        assertEquals(url.toString(), "https://example.com/tests/results/abcd1234");
    }
}