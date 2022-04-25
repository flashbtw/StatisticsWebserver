package de.flashyboi.minecraft.plugins.statisticswebserver;

import io.undertow.Undertow;
import io.undertow.util.Headers;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class StatisticsWebserver extends JavaPlugin {
    private static final String KILL_QUERY = "kills";
    private static final String DEATH_QUERY = "deaths";
    private static final String BAD_REQUEST = "<html><body><h1><center>400 Bad Request</center></h1></body></html>";

    @Override
    public void onEnable() {
        // Plugin startup logic
        Undertow server = Undertow.builder()
                .addHttpListener(7272, "0.0.0.0")
                .setHandler(exchange -> {
                    exchange.getResponseHeaders()
                            .put(Headers.CONTENT_TYPE, "text/html");
                    Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                    try {
                        String uuidQuery = queryParams.get("UUID").element();
                        UUID uuid = UUID.fromString(uuidQuery);
                        String statisticQuery = queryParams.get("stat").element();
                        String response;
                        switch (statisticQuery) {
                            case KILL_QUERY -> response = PlayerStatisticsManager.getPlayerKills(uuid);
                            case DEATH_QUERY -> response = PlayerStatisticsManager.getPlayerDeaths(uuid);
                            default -> response = BAD_REQUEST;
                        }
                        exchange.getResponseSender()
                                .send(response);
                    } catch (IllegalArgumentException | NullPointerException e) {
                        exchange.getResponseSender().send(BAD_REQUEST);
                    }
                }).build();

        server.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

class PlayerStatisticsManager {
    public static String getPlayerKills(UUID playerUuid) {
        try {
            return String.valueOf(Bukkit.getPlayer(playerUuid).getStatistic(Statistic.PLAYER_KILLS));
        } catch (NullPointerException npe) {
            return String.valueOf(Bukkit.getOfflinePlayer(playerUuid).getStatistic(Statistic.PLAYER_KILLS));
        }
    }

    public static String getPlayerDeaths(UUID playerUuid) {
        try {
            return String.valueOf(Bukkit.getPlayer(playerUuid).getStatistic(Statistic.DEATHS));
        } catch (NullPointerException npe) {
            return String.valueOf(Bukkit.getOfflinePlayer(playerUuid).getStatistic(Statistic.DEATHS));
        }
    }
}