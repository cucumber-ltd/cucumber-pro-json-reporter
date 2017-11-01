package io.cucumber.pro.documentation;

import io.cucumber.pro.Env;
import io.cucumber.pro.URITemplate;

import java.util.HashMap;

class CucumberProGitRemoteBuilder {
    private static final String DEFAULT_GIT_HOST = "git.cucumber.pro";
    private static final String ENV_CUCUMBER_PRO_GIT_HOST = "CUCUMBER_PRO_GIT_HOST";

    static String buildCucumberProUrl(final Env env, final String projectName) {
        String template = "git@{gitHost}:{projectName}.git";
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("gitHost", env.get(ENV_CUCUMBER_PRO_GIT_HOST, DEFAULT_GIT_HOST));
            put("projectName", projectName);
        }});
    }
}
