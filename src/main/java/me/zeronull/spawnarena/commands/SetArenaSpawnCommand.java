package me.zeronull.spawnarena.commands;

import me.zeronull.spawnarena.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        int spawnNumber = Integer.parseInt(args[0]);

        if(spawnNumber != 1 && spawnNumber != 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cImproper spawn point number!"));
            return true;
        }

        Location location = player.getLocation();
        ConfigHandler configHandler = ConfigHandler.getInstance();
        configHandler.setSpawnpoint(location, spawnNumber);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
        return true;
    }
    
}
