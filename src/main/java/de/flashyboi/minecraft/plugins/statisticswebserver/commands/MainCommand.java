package de.flashyboi.minecraft.plugins.statisticswebserver.commands;

import de.flashyboi.minecraft.plugins.statisticswebserver.StatisticsWebserver;
import de.flashyboi.minecraft.plugins.statisticswebserver.config.ConfigManager;
import de.flashyboi.minecraft.plugins.statisticswebserver.util.PluginInteractor;
import io.github.flashbtw.enums.ConfigActions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends PluginInteractor implements CommandExecutor, TabCompleter {
    private static final List<String> TAB_COMPLETES = new ArrayList<>();
    static {
        TAB_COMPLETES.add("reload");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("statisticswebserver.reload")) {
                    System.out.println(StatisticsWebserver.configManager.action(ConfigActions.GET_VALUE, "Webserver-Config.host", String.class));
                    getPlugin().reloadConfig();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("statisticswebserver.tabcomplete")) {
            return TAB_COMPLETES;
        }
        return null;
    }
}