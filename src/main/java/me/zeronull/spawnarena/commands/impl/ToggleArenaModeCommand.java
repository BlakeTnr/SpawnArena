package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.ArenaMode;
import me.zeronull.spawnarena.SavableArena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.commands.ArenaTabComplete;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ToggleArenaModeCommand extends ArenaTabComplete implements CommandExecutor {
    public ToggleArenaModeCommand() {
        super(0);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /togglearenamode <name>"));
            return true;
        }

        final String arenaName = args[0];

        Player player = (Player) sender;

        if (!(player.getLocation().getWorld().getName().equalsIgnoreCase(Arena.SPAWN_WORLD))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be at spawn to use this command!"));
            return true;
        }

        final SavableArena arena = SpawnArena.arenas.of(arenaName).to(SavableArena.class);

        if (arena == null) {
            sender.sendMessage(ChatColor.RED + "That arena was not found.");
            return true;
        }

        if (!arena.isSetUp()) {
            sender.sendMessage(ChatColor.RED + "That arena has not been fully setup!");
            return true;
        }

        final ArenaMode curentArenaMode = arena.getArenaMode();
        final ArenaMode next = curentArenaMode.next();

        arena.setArenaMode(next.toString());
        arena.save(config -> config.arenaMode = next.toString());

        player.sendMessage(ChatColor.GREEN + String.format("Set arena mode to '%s'.", next.name()));

        return true;
    }
}