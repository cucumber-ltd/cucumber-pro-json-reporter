package io.cucumber.pro.revision;

import io.cucumber.pro.Env;
import io.cucumber.pro.TestLogger;
import io.cucumber.pro.config.Config;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RevisionProviderFactoryTest {
    @Test
    public void finds_revision_from_environment_variable() {
        Config config = new Config();
        config.set(Env.bamboo_planRepository_revision, "foo");
        RevisionProvider revisionProvider = RevisionProviderFactory.create(config, null);
        assertEquals("foo", revisionProvider.getRevision());
    }

    @Test
    public void falls_back_to_find_revision_from_git() {
        Config config = new Config();
        RevisionProvider revisionProvider = RevisionProviderFactory.create(config, new TestLogger());
        assertTrue(
                String.format("Not a git revision: %s", revisionProvider.getRevision()),
                revisionProvider.getRevision().matches("[a-f0-9]{40}")
        );
    }
}