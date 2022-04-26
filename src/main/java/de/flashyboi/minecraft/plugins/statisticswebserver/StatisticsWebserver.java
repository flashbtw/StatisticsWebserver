package de.flashyboi.minecraft.plugins.statisticswebserver;

import de.flashyboi.minecraft.plugins.statisticswebserver.exceptions.PlayerNotFoundException;
import io.undertow.Undertow;
import io.undertow.util.Headers;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public final class StatisticsWebserver extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        WebserverManager.startWebserver(7272, "0.0.0.0");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

class WebserverManager {
    private static final String KILL_QUERY = "kills";
    private static final String DEATH_QUERY = "deaths";
    protected static void startWebserver(int port, String host) {
        Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(exchange -> {
                    exchange.getResponseHeaders()
                            .put(Headers.CONTENT_TYPE, "text/html");
                    Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                    try {
                        String uuidQuery = queryParams.get("UUID").element();
                        UUID uuid = UUID.fromString(uuidQuery);
                        System.out.println(uuid);
                        String statisticQuery = queryParams.get("stat").element();
                        String response = "";
                        switch (statisticQuery) {
                            case KILL_QUERY:
                                try {
                                    response = PlayerStatisticsManager.getPlayerKills(uuid);
                                } catch (PlayerNotFoundException playerNotFoundException) {
                                    exchange.setStatusCode(404);
                                }
                                break;
                            case DEATH_QUERY:
                                try {
                                    response = PlayerStatisticsManager.getPlayerDeaths(uuid);
                                } catch (PlayerNotFoundException playerNotFoundException) {
                                    exchange.setStatusCode(404);
                                }
                                break;
                            default:
                                exchange.setStatusCode(400);
                        }
                        exchange.getResponseSender()
                                .send(response);

                    } catch (IllegalArgumentException | NullPointerException e) {
                        exchange.setStatusCode(400);
                    }
                }).build();

        server.start();
    }
}

class PlayerStatisticsManager {
    public static String getPlayerKills(UUID playerUuid) throws PlayerNotFoundException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUuid);
        try {
            System.out.println(player.hasPlayedBefore());
            if (!player.hasPlayedBefore()) {
                throw new PlayerNotFoundException();
            }
            return String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS));
        } catch (NullPointerException npe) {

            return String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS));
        }
    }

    public static String getPlayerDeaths(UUID playerUuid) throws PlayerNotFoundException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUuid);
        try {
            System.out.println(player.hasPlayedBefore());
            if (!player.hasPlayedBefore()) {
                throw new PlayerNotFoundException();
            }
            return String.valueOf(player.getStatistic(Statistic.DEATHS));
        } catch (NullPointerException npe) {

            return String.valueOf(player.getStatistic(Statistic.DEATHS));
        }
    }
}