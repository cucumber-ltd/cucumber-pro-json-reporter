package io.cucumber.pro;

import io.cucumber.pro.config.Config;

public class Keys {
    public static final String CUCUMBERPRO_CONNECTION_IGNOREERROR = "cucumberpro.connection.ignoreerror";
    public static final String CUCUMBERPRO_CONNECTION_TIMEOUT = "cucumberpro.connection.timeout";
    public static final String CUCUMBERPRO_ENVMASK = "cucumberpro.envmask";
    public static final String CUCUMBERPRO_LOGFILE = "cucumberpro.logfile";
    public static final String CUCUMBERPRO_LOGGING = "cucumberpro.logging";
    public static final String CUCUMBERPRO_PROFILE = "cucumberpro.profile";
    public static final String CUCUMBERPRO_PROJECTNAME = "cucumberpro.projectname";
    public static final String CUCUMBERPRO_TOKEN = "cucumberpro.token";
    public static final String CUCUMBERPRO_URL = "cucumberpro.url";

    public static Config createConfig() {
        Config config = new Config();
        config.set(CUCUMBERPRO_CONNECTION_IGNOREERROR, true);
        config.set(CUCUMBERPRO_CONNECTION_TIMEOUT, 5000);
        config.set(CUCUMBERPRO_ENVMASK, "SECRET|KEY|TOKEN|PASSWORD");
        config.setNull(CUCUMBERPRO_LOGFILE);
        config.set(CUCUMBERPRO_LOGGING, "INFO");
        config.setNull(CUCUMBERPRO_PROFILE);
        config.setNull(CUCUMBERPRO_PROJECTNAME);
        config.setNull(CUCUMBERPRO_TOKEN);
        config.set(CUCUMBERPRO_URL, "https://app.cucumber.pro/");

        return config;
    }
}
