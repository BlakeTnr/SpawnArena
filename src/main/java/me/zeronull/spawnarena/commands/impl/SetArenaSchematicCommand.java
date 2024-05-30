package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.SavableArena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.WorldEditUtils;
import me.zeronull.spawnarena.commands.ArenaTabComplete;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public final class SetArenaSchematicCommand extends ArenaTabComplete implements CommandExecutor {
    public SetArenaSchematicCommand() {
        super(1);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /setarenaschematic <schematic> <arena>"));
            return true;
        }

        final String schematicName = args[0];
        String arenaName = args[1];

        if (!SpawnArena.arenas.exists(arenaName)) {
            sender.sendMessage(ChatColor.RED + "That arena does not exist!");
            return true;
        }

        final SavableArena arena = SpawnArena.arenas.of(arenaName).to(SavableArena.class);
        final File schematic = WorldEditUtils.getSchematic(schematicName);

        if (!schematic.exists()) {
            sender.sendMessage(ChatColor.RED + "Schematic not found.");
            return true;
        }

        arena.setSchematic(schematicName);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aAssigned shematic '&6%s&a' to the arena '&6%s&a'.", schematic.getPath(), arena.getArenaName())));

        return true;
    }
}