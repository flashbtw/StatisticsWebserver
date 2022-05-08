package de.flashyboi.minecraft.plugins.statisticswebserver.exceptions;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException() {
        super();
    }

    public PlayerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

