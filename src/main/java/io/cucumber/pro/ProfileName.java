package io.cucumber.pro;

import io.cucumber.pro.config.Config;

import static io.cucumber.pro.Keys.CUCUMBERPRO_PROFILE;

public class ProfileName {
    static String getProfileName(Config config, String profileName) {
        if (!config.isNull(CUCUMBERPRO_PROFILE)) {
            return config.getString(CUCUMBERPRO_PROFILE);
        } else if (profileName != null) {
            return profileName;
        } else {
            return "default";
        }
    }
}
