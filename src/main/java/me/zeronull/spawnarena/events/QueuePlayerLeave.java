package me.zeronull.spawnarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.zeronull.spawnarena.SpawnArena;

public class QueuePlayerLeave implements Listener {
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(SpawnArena.arena.queue.playerInQueue(player)) {
            SpawnArena.arena.queue.removePlayer(player);
        }
    }

}
