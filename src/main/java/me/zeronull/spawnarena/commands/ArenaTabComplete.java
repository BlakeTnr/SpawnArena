package me.zeronull.spawnarena.commands;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ArenaTabComplete implements TabCompleter {
    private final int index;
    private final boolean allowPlayersAfter;
    private final int allowPlayersUntil;

    protected ArenaTabComplete(final int index) {
        this(index, false, 0);
    }

    protected ArenaTabComplete(final int index, final boolean allowPlayersAfter, final int allowPlayersUntil) {
        this.index = index;
        this.allowPlayersAfter = allowPlayersAfter;
        this.allowPlayersUntil = allowPlayersUntil;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == index + 1) {
            return SpawnArena.arenas.toList().stream()
                    .map(Arena::getArenaName)
                    .filter(name -> name.toLowerCase().startsWith(args[index].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (this.allowPlayersAfter && args.length > allowPlayersUntil)
            return new ArrayList<>();

        return !this.allowPlayersAfter ? new ArrayList<>() : Bukkit.getOnlinePlayers().stream()
                .map(p -> p.getName())
                .filter(name -> name.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}