package io.cucumber.pro.results;

import io.cucumber.pro.URITemplate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class URITemplateTest {
    @Test
    public void expands_arguments() {
        Map<String, String> values = new HashMap<String, String>() {{
            put("projectName", "bar");
            put("revision", "zap");
        }};
        URITemplate uriTemplate = new URITemplate("http://example.com/foo/{projectName}/{revision}");
        assertEquals("http://example.com/foo/bar/zap", uriTemplate.expand(values));
    }

    @Test(expected = RuntimeException.class)
    public void throws_exception_for_missing_argument() {
        Map<String, String> values = new HashMap<String, String>() {{
            put("projectName", "bar");
        }};
        URITemplate uriTemplate = new URITemplate("http://example.com/foo/{projectName}/{revision}");
        uriTemplate.expand(values);
    }
}
