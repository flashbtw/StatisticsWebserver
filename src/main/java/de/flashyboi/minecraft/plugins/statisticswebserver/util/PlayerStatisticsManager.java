package de.flashyboi.minecraft.plugins.statisticswebserver.util;

import de.flashyboi.minecraft.plugins.statisticswebserver.exceptions.PlayerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.UUID;

public class PlayerStatisticsManager {
    private static final String PLAYER_NEVER_PLAYED = "player has never played on this server before";
    /**
     * returns the number of kills the player made.
     * If the player didn't play before, the method throws a PlayerNotFoundException
     */
    public static String getPlayerKills(UUID playerUuid) throws PlayerNotFoundException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUuid);
        try {
            if (!player.hasPlayedBefore()) {
                throw new PlayerNotFoundException(PLAYER_NEVER_PLAYED);
            }
            return String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS));
        } catch (NullPointerException npe) {

            return String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS));
        }
    }

    /**
     * returns the number of deaths the player has.
     * If the player didn't play before, the method throws a PlayerNotFoundException
     */
    public static String getPlayerDeaths(UUID playerUuid) throws PlayerNotFoundException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUuid);
        try {
            if (!player.hasPlayedBefore()) {
                throw new PlayerNotFoundException(PLAYER_NEVER_PLAYED);
            }
            return String.valueOf(player.getStatistic(Statistic.DEATHS));
        } catch (NullPointerException npe) {

            return String.valueOf(player.getStatistic(Statistic.DEATHS));
        }
    }
}
