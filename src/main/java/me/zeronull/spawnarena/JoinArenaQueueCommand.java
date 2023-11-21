package me.zeronull.spawnarena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class JoinArenaQueueCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        if(!sender.hasPermission("spawnarena.joinarenaqueue")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));
            return true;
        }

        // If extending in future, add arg for which arena

        Player player = (Player) sender;

        if(!(player.getLocation().getWorld().getName().equalsIgnoreCase("SpawnWorld"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be at spawn to use this command!"));
            return true;
        }

        if(ArenaQueue.getInstance().playerInQueue(player)) {
            ArenaQueue.getInstance().removePlayer(player);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou've been removed from the queue!"));
            return true;
        }

        ArenaQueue.getInstance().addPlayerToQueue(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou've been added to the queue!"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eWarning! If you have issues (e.g. losing inventory, want you inventory restored due to other issue), &c&l&ndo NOT&e rejoin the arena, tell warn staff and players, and avoid dying excessive amount of times. Otherwise, your inventory logs will be lost!"));
        return true;
    }
    
}
