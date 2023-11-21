package me.zeronull.spawnarena.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.zeronull.spawnarena.SpawnArena;

public class PlayerLeaveSpawn implements Listener {
    @EventHandler
    public void onPlayerLeaveSpawn(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if(!SpawnArena.arena.queue.playerInQueue(player)) {
            return;
        }

        if(event.getTo().getWorld().getName().equalsIgnoreCase("SpawnWorld")) {
            return;
        }

        SpawnArena.arena.queue.removePlayer(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou've been removed from arena queue. You must stay in spawn to stay in queue."));
    }
}
