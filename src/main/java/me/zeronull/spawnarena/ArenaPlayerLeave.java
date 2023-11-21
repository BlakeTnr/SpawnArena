package me.zeronull.spawnarena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArenaPlayerLeave implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = (Player) event.getPlayer();

        if(!SpawnArena.arena.isFighter(player)) {
            return;
        }

        if(SpawnArena.arena.isInitializing) {
            return;
        }

        SpawnArena.arena.announceWinner(player);
        SpawnArena.arena.endFight();
    }
    
}
