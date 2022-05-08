package de.flashyboi.minecraft.plugins.statisticswebserver.staticvar;

public enum Messages {

    DEBUG_MESSAGE_WEBSERVER("Incoming Request! UUID Query: %s; Statistic Query: %s; Response: %s"),
    DEBUG_MESSAGE_INVALID_REQUEST("Invalid Request! Query: %s"),
    DEBUG_MESSAGE_PLAYER_NOT_FOUND("Player never played before! Query: %s");

    private String message;
    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
