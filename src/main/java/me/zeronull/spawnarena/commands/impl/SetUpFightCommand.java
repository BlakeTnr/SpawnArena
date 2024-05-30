package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.commands.ArenaTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class SetUpFightCommand extends ArenaTabComplete implements CommandExecutor {
    public SetUpFightCommand() {
        super(0, true, 3);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /setupfight <arena> <fighter1> <fighter2>"));
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

        final String fighter1Username = args[1];
        final String fighter2Username = args[2];

        final Player fighter1 = Bukkit.getPlayer(fighter1Username);
        final Player fighter2 = Bukkit.getPlayer(fighter2Username);

        if (fighter1 == null || !fighter1.isOnline() || fighter2 == null || !fighter2.isOnline()) {
            sender.sendMessage(ChatColor.RED + "One or more of the specified fighters are not online.");
            return true;
        }

        final Predicate<Player> isInFight = player -> arena.getFight(player) != null;

        if (isInFight.test(fighter1) || isInFight.test(fighter2)) {
            sender.sendMessage(ChatColor.RED + "One of the specified players is already in a fight.");
            return true;
        }

        final Consumer<Player> call = fighter -> {
          if (SpawnArena.arenas.isInQueue(fighter, arena.queue))
              arena.queue.removePlayer(fighter);
        };

        call.accept(fighter1);
        call.accept(fighter2);

        JoinArenaQueueCommand.addToQueue(arena, fighter1);
        JoinArenaQueueCommand.addToQueue(arena, fighter2);

        sender.sendMessage(ChatColor.GREEN + String.format("The fight between %s and %s will begin in 5 seconds.", fighter1.getName(), fighter2.getName()));
        return true;
    }
}