package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.ArenaQueue;
import me.zeronull.spawnarena.SavableArena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.commands.errors.SenderNotPlayerException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CreateArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            validateSenderIsPlayer(sender);

            if (args.length < 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /createarena <name>"));
                return true;
            }

            Player player = (Player) sender;

            if (!(player.getLocation().getWorld().getName().equalsIgnoreCase(Arena.SPAWN_WORLD))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be at spawn to use this command!"));
                return true;
            }

            final String arenaName = args[0];

            if (arenaName.length() < 2) {
                sender.sendMessage(ChatColor.RED + "Provided arena name is too short.");
                return true;
            }

            final SavableArena arena = new SavableArena(arenaName);
            SavableArena.createArenaConfig(arena.getConfigFile());
            arena.save(config -> config.arenaName = arena.getArenaName());

            final ArenaQueue queue = new ArenaQueue(arena);
            arena.setQueue(queue);

            SpawnArena.arenas.add(arena);

            sender.sendMessage(ChatColor.GREEN + String.format("Created arena '%s'!", arena.getArenaName()));
            return true;
        } catch (SenderNotPlayerException e) {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
            return true;
        }
    }

    public void validateSenderIsPlayer(CommandSender sender) throws SenderNotPlayerException {
        if (sender instanceof Player)
            return;

        throw new SenderNotPlayerException("Sender not player");
    }

}