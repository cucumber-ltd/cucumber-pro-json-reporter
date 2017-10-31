package io.cucumber.pro.metadata;

import java.util.List;

class ChainedMetadata implements Metadata {

    private final List<Metadata> chain;

    ChainedMetadata(List<Metadata> chain) {
        this.chain = chain;
    }

    @Override
    public String getProjectName() {
        for (Metadata metadata : chain) {
            String result = metadata.getProjectName();
            if (result != null) return result;
        }
        return null;
    }
}
