package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.commands.ArenaTabComplete;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinArenaQueueCommand extends ArenaTabComplete implements CommandExecutor {
    public JoinArenaQueueCommand() {
        super(0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /joinarenaqueue <name>"));
            return true;
        }

        final String arenaName = args[0];
        final Arena arena = SpawnArena.arenas.of(arenaName);

        if (arena == null) {
            sender.sendMessage(ChatColor.RED + "That arena was not found.");
            return true;
        }

        if (!arena.isSetUp()) {
            sender.sendMessage(ChatColor.RED + "That arena has not been fully setup!");
            return true;
        }

        Player player = (Player) sender;

        if (SpawnArena.arenas.isInQueue(player, arena.queue)) {
            sender.sendMessage(ChatColor.RED + "You are already queued for another arena.");
            return true;
        }

        if (SpawnArena.arenas.hasFighter(player)) {
            sender.sendMessage(ChatColor.RED + "You are already in a fight!");
            return true;
        }

        if (!(player.getLocation().getWorld().getName().equalsIgnoreCase(Arena.SPAWN_WORLD))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be at spawn to use this command!"));
            return true;
        }

        if (arena.queue.playerInQueue(player)) {
            arena.queue.removePlayer(player);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou've been removed from the queue!"));
            return true;
        }

        addToQueue(arena, player);
        return true;
    }

    public static void addToQueue(final Arena arena, final Player player) {
        arena.queue.addPlayerToQueue(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou've been added to the queue!"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eWarning! If you have issues (e.g. losing inventory, want you inventory restored due to other issue), &c&l&ndo NOT&e rejoin the arena, tell warn staff and players, and avoid dying excessive amount of times. Otherwise, your inventory logs will be lost!"));
    }
}
