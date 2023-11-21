package me.zeronull.spawnarena;

import java.io.File;
import java.nio.file.Path;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SetArenaSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        if(!sender.hasPermission("spawnarena.setarenaspawn")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));
            return true;
        }
        
        if(args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /setarenaspawn (1/2)"));
            return true;
        }

        Player player = (Player) sender;

        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);

        int spawnNumber = Integer.parseInt(args[0]);
        if(spawnNumber == 1) {
            GameConfig config = plugin.getCustomConfig();
            config.spawnPoint1 = player.getLocation();
            plugin.saveCustomConfig(config);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
            return true;
        } else if(spawnNumber == 2) {
            GameConfig config = plugin.getCustomConfig();
            config.spawnPoint2 = player.getLocation();
            plugin.saveCustomConfig(config);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
            return true;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cImproper usage!"));
            return true;
        }
    }
    
}
