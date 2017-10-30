package io.cucumber.pro.publisher;

import io.cucumber.pro.revision.RevisionProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTTPPublisherTest {
    @Test
    public void builds_url_without_slash() {
        HTTPPublisher httpPublisher = new HTTPPublisher("https://example.com/tests/results/", new RevisionProvider() {
            @Override
            public String getRev() {
                return "abcd1234";
            }
        });
        assertEquals("https://example.com/tests/results/abcd1234", httpPublisher.getUri().toString());
    }

    @Test
    public void builds_url_with_slash() {
        HTTPPublisher httpPublisher = new HTTPPublisher("https://example.com/tests/results", new RevisionProvider() {
            @Override
            public String getRev() {
                return "abcd1234";
            }
        });
        assertEquals("https://example.com/tests/results/abcd1234", httpPublisher.getUri().toString());
    }
}