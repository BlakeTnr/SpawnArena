package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.PlayerPreFightData;
import me.zeronull.spawnarena.config.ConfigHandler;
import me.zeronull.spawnarena.config.impl.PreFightConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class RestoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /restore <player>"));
            return true;
        }

        final String username = args[0];
        final Player player = Bukkit.getPlayer(username);

        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        final ConfigHandler handler = ConfigHandler.getInstance();
        final PreFightConfig config = handler.getPreFightConfig();
        final PlayerPreFightData data = config.map.get(player.getUniqueId());

        if (data == null) {
            sender.sendMessage(ChatColor.RED + "Data not found.");
            return true;
        }

        data.player = player;
        data.restore();

        config.map.remove(player.getUniqueId());
        handler.savePreFightConfig(config);

        sender.sendMessage(ChatColor.GREEN + "Attempted to restore the items of %s.", player.getName());
        return true;
    }
}