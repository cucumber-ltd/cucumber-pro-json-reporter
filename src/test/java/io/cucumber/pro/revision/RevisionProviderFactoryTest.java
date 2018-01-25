package io.cucumber.pro.revision;

import io.cucumber.pro.Keys;
import io.cucumber.pro.TestLogger;
import io.cucumber.pro.config.Config;
import org.junit.Test;

import static io.cucumber.pro.Keys.createConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RevisionProviderFactoryTest {
    @Test
    public void finds_revision_from_environment_variable() {
        Config config = createConfig();
        config.set(Keys.bamboo_planRepository_revision, "foo");
        RevisionProvider revisionProvider = RevisionProviderFactory.create(config, null);
        assertEquals("foo", revisionProvider.getRevision());
    }

    @Test
    public void falls_back_to_find_revision_from_git() {
        Config config = createConfig();
        RevisionProvider revisionProvider = RevisionProviderFactory.create(config, new TestLogger());
        assertTrue(
                String.format("Not a git revision: %s", revisionProvider.getRevision()),
                revisionProvider.getRevision().matches("[a-f0-9]{40}")
        );
    }
}