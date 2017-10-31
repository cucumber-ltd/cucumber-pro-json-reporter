package io.cucumber.pro.metadata;

public class NullMetadata implements Metadata {
    @Override
    public String getProjectName() {
        return null;
    }
}
