package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerLeaveSpawn implements Listener {
    @EventHandler
    public void onPlayerLeaveSpawn(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (!SpawnArena.arenas.isInQueue(player)) {
            return;
        }

        if (event.getTo().getWorld().getName().equalsIgnoreCase(Arena.SPAWN_WORLD)) {
            return;
        }

        SpawnArena.arenas.fromQueued(player).queue.removePlayer(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou've been removed from arena queue. You must stay in spawn to stay in queue."));
    }
}
