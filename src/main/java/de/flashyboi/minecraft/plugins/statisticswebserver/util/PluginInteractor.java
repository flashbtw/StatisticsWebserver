package de.flashyboi.minecraft.plugins.statisticswebserver.util;

import de.flashyboi.minecraft.plugins.statisticswebserver.StatisticsWebserver;
import org.bukkit.plugin.Plugin;

public class PluginInteractor {
    private static Plugin plugin;
    public static void setPlugin(StatisticsWebserver statisticsWebserver) {
        plugin = statisticsWebserver;
    }
    public static Plugin getPlugin() throws NullPointerException {
        if (plugin != null) {
            return plugin;
        }
        throw new NullPointerException();
    }
}
