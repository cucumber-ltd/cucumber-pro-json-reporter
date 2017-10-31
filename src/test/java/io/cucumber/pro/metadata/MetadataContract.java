package io.cucumber.pro.metadata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class MetadataContract {
    @Test
    public void provides_project_name() throws Exception {
        Metadata metadata = createMetadata();
        String projectName = metadata.getProjectName();
        assertEquals("cucumber-pro-plugin-jvm", projectName);
    }

    protected abstract Metadata createMetadata() throws Exception;
}
