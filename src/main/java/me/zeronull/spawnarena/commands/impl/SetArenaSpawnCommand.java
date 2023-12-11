package me.zeronull.spawnarena.commands.impl;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.SavableArena;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.commands.ArenaTabComplete;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetArenaSpawnCommand extends ArenaTabComplete implements CommandExecutor {
    public SetArenaSpawnCommand() {
        super(1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }
        
        if(args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /setarenaspawn (1/2) <arena>"));
            return true;
        }

        Player player = (Player) sender;

        int spawnNumber = Integer.parseInt(args[0]);
        String arenaName = args[1];

        if (!SpawnArena.arenas.exists(arenaName)) {
            sender.sendMessage(ChatColor.RED + "That arena does not exist!");
            return true;
        }

        final SavableArena arena = SpawnArena.arenas.of(arenaName).to(SavableArena.class);

        if(spawnNumber != 1 && spawnNumber != 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cImproper spawn point number!"));
            return true;
        }

        final Location loc = player.getLocation();
        this.setSpawnpoint(arena, loc, spawnNumber);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
        return true;
    }

    private void setSpawnpoint(final Arena arena, final Location loc, final int spawnNumber) {
        if (spawnNumber == 1)
            arena.setSpawnPoint1(loc);
        else if (spawnNumber == 2)
            arena.setSpawnPoint2(loc);
    }
}
