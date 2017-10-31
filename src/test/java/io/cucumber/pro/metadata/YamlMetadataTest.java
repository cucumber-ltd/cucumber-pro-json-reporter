package io.cucumber.pro.metadata;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class YamlMetadataTest extends MetadataContract {
    @Override
    protected Metadata createMetadata() throws UnsupportedEncodingException {
        Reader source = new InputStreamReader(getClass().getResourceAsStream(".cucumberpro.yml"), "utf-8");
        return new YamlMetadata(source);
    }
}
