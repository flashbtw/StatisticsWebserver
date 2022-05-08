package de.flashyboi.minecraft.plugins.statisticswebserver.util;

import de.flashyboi.minecraft.plugins.statisticswebserver.StatisticsWebserver;
import de.flashyboi.minecraft.plugins.statisticswebserver.exceptions.PlayerNotFoundException;
import de.flashyboi.minecraft.plugins.statisticswebserver.staticvar.ConfigPaths;
import de.flashyboi.minecraft.plugins.statisticswebserver.staticvar.Messages;
import io.github.flashbtw.enums.ConfigActions;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.Deque;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class WebserverManager {
    private static final String KILL_QUERY = "kills";
    private static final String DEATH_QUERY = "deaths";

    /** starts a webserver using two arguments.
     *
     * @param port - represents the port the webserver will be accessible on
     * @param host - represents the IP the server will be hosted on
     */
    public static void startWebserver(int port, String host) {
        Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(exchange -> {

                    exchange.getResponseHeaders()
                            .put(Headers.CONTENT_TYPE, "text/html");
                    Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                    boolean enabledDebug = StatisticsWebserver.configManager.action(ConfigActions.GET_VALUE, ConfigPaths.DEBUG_LOG.getPath(), Boolean.class);
                    analyzeQuery(exchange, queryParams, enabledDebug);

                }).build();

        server.start();
    }

    private static void analyzeQuery(HttpServerExchange exchange, Map<String, Deque<String>> queryParams, boolean enabledDebug) {
        try {
            String uuidQuery = queryParams.get("UUID").element();
            String statisticQuery = queryParams.get("stat").element();
            UUID uuid = UUID.fromString(uuidQuery);
            String response = "";

            switch (statisticQuery) {
                case KILL_QUERY:
                    response = getKillResponse(exchange, queryParams, enabledDebug, uuidQuery, statisticQuery, uuid, response);
                    break;
                case DEATH_QUERY:
                    response = getDeathResponse(exchange, queryParams, enabledDebug, uuidQuery, statisticQuery, uuid, response);
                    break;
                default:
                    sendInvalidRequestDebugMessage(queryParams, enabledDebug);
                    exchange.setStatusCode(400);
            }
            exchange.getResponseSender()
                    .send(response);

        } catch (IllegalArgumentException | NoSuchElementException e) {
            sendInvalidRequestDebugMessage(queryParams, enabledDebug);
            exchange.setStatusCode(400);
        } catch (NullPointerException npe) {
            //do nothing because it's just random if it's being thrown or not with the exact same params.
        }
    }

    private static void sendInvalidRequestDebugMessage(Map<String, Deque<String>> queryParams, boolean enabledDebug) {
        if (enabledDebug) {
            StatisticsWebserver.log.severe(String.format(Messages.DEBUG_MESSAGE_INVALID_REQUEST.getMessage(), queryParams));
        }
    }

    private static String getDeathResponse(HttpServerExchange exchange, Map<String, Deque<String>> queryParams, boolean enabledDebug, String uuidQuery, String statisticQuery, UUID uuid, String response) {
        try {
            response = PlayerStatisticsManager.getPlayerDeaths(uuid);
            sendSuccessfulDebugMessage(enabledDebug, uuidQuery, statisticQuery, response);
        } catch (PlayerNotFoundException playerNotFoundException) {
            sendPlayerNotFoundError(exchange, queryParams, enabledDebug);
        }
        return response;
    }

    private static String getKillResponse(HttpServerExchange exchange, Map<String, Deque<String>> queryParams, boolean enabledDebug, String uuidQuery, String statisticQuery, UUID uuid, String response) {
        try {
            response = PlayerStatisticsManager.getPlayerKills(uuid);
            sendSuccessfulDebugMessage(enabledDebug, uuidQuery, statisticQuery, response);
        } catch (PlayerNotFoundException playerNotFoundException) {
            sendPlayerNotFoundError(exchange, queryParams, enabledDebug);
        }
        return response;
    }

    private static void sendSuccessfulDebugMessage(boolean enabledDebug, String uuidQuery, String statisticQuery, String response) {
        if (enabledDebug) {
            StatisticsWebserver.log.info(String.format(Messages.DEBUG_MESSAGE_WEBSERVER.getMessage(), uuidQuery, statisticQuery, response));
        }
    }

    private static void sendPlayerNotFoundError(HttpServerExchange exchange, Map<String, Deque<String>> queryParams, boolean enabledDebug) {
        if (enabledDebug) {
            StatisticsWebserver.log.severe(String.format(Messages.DEBUG_MESSAGE_PLAYER_NOT_FOUND.getMessage(), queryParams));
        }
        exchange.setStatusCode(404);
    }

}
