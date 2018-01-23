package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.URITemplate;
import io.cucumber.pro.config.Config;

import java.util.HashMap;

class CucumberProGitRemoteBuilder {
    private static final String DEFAULT_GIT_HOST = "git.cucumber.pro";

    static String buildCucumberProUrl(final Config config, final String projectName) {
        String template = "git@{gitHost}:{projectName}.git";
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("gitHost", config.get(Env.CUCUMBER_PRO_GIT_HOST, DEFAULT_GIT_HOST));
            put("projectName", projectName);
        }});
    }
}
