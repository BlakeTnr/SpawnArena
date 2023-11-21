package me.zeronull.spawnarena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class ArenaPlayerLeaveAreaEvent implements Listener {

    @EventHandler
    public void onPlayerTeleportOut(PlayerTeleportEvent event) {
        Player player = (Player) event.getPlayer();

        if(!SpawnArena.arena.isFighter(player)) {
            return;
        }

        if(SpawnArena.arena.isInitializing || SpawnArena.arena.isEnding) {
            return;
        }

        if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN) {
            SpawnArena.arena.announceWinner(player);
            SpawnArena.arena.endFight();
        }
    }
    
}

