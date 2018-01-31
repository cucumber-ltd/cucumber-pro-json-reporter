package io.cucumber.pro.revision;

import io.cucumber.pro.Logger;
import io.cucumber.pro.config.Config;
import io.cucumber.pro.environment.CIEnvironment;

public class RevisionProviderFactory {
    public static RevisionProvider create(final Config config, Logger logger) {
        CIEnvironment ciEnvironment = CIEnvironment.detect(config);
        if (ciEnvironment != null) {
            return ciEnvironment.getRevisionProvider(config);
        } else {
            return new GitRevisionProvider(logger);
        }
    }
}
