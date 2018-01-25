package io.cucumber.pro.documentation;

import io.cucumber.pro.Keys;
import io.cucumber.pro.URITemplate;
import io.cucumber.pro.config.Config;

import java.util.HashMap;

class CucumberProGitRemoteBuilder {

    static String buildCucumberProUrl(final Config config, final String projectName) {
        String template = "git@{gitHost}:{projectName}.git";
        URITemplate uriTemplate = new URITemplate(template);
        return uriTemplate.expand(new HashMap<String, String>() {{
            put("gitHost", config.getString(Keys.CUCUMBERPRO_GIT_HOSTNAME));
            put("projectName", projectName);
        }});
    }
}
