package de.flashyboi.minecraft.plugins.statisticswebserver.staticvar;

public enum ConfigPaths {

    VERSION("version"),
    DEBUG_LOG("debug-log"),
    WEBSERVER_PORT("Webserver-Config.port"),
    WEBSERVER_HOST("Webserver-Config.host");

    private String path;
    ConfigPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
