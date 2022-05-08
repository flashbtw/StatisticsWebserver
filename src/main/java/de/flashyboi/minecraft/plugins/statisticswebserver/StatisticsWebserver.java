package de.flashyboi.minecraft.plugins.statisticswebserver;

import de.flashyboi.minecraft.plugins.statisticswebserver.commands.MainCommand;
import de.flashyboi.minecraft.plugins.statisticswebserver.config.ConfigManager;
import de.flashyboi.minecraft.plugins.statisticswebserver.staticvar.ConfigPaths;
import de.flashyboi.minecraft.plugins.statisticswebserver.util.PluginInteractor;
import de.flashyboi.minecraft.plugins.statisticswebserver.util.WebserverManager;
import io.github.flashbtw.enums.ConfigActions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class StatisticsWebserver extends JavaPlugin {
    public static ConfigManager configManager;
    public static Logger log;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initializeServices();

        int webserverPort = configManager.action(ConfigActions.GET_VALUE, ConfigPaths.WEBSERVER_PORT.getPath(), Integer.class);
        String webserverHost = configManager.action(ConfigActions.GET_VALUE, ConfigPaths.WEBSERVER_HOST.getPath(), String.class);

        WebserverManager.startWebserver(webserverPort, webserverHost);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initializeServices() {
        log = this.getLogger();
        configManager = new ConfigManager(this);
        PluginInteractor.setPlugin(this);
        initializeCommands();
        this.saveDefaultConfig();
    }

    private void initializeCommands() {
        try {
            this.getCommand("statisticswebserver").setExecutor(new MainCommand());
            this.getCommand("statisticswebserver").setTabCompleter(new MainCommand());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }
}


//TODO: do config stuff