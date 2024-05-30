package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.SavableArena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.commands.ArenaTabComplete;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetPasteLocationCommand extends ArenaTabComplete implements CommandExecutor {
    public SetPasteLocationCommand() {
        super(0);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        final Player player = (Player) sender;

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /setpastelocation <arena>");
            return true;
        }

        final String arenaName = args[0];

        if (!SpawnArena.arenas.exists(arenaName)) {
            sender.sendMessage(ChatColor.RED + "That arena does not exist!");
            return true;
        }

        final SavableArena arena = SpawnArena.arenas.of(arenaName).to(SavableArena.class);
        final Location loc = player.getLocation();

        arena.setPasteLocation(loc);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aSet the paste location of the arena '&6%s&a'.", arena.getArenaName())));

        return true;
    }
}