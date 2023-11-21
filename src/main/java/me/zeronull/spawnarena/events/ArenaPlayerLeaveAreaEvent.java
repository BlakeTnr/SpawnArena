package me.zeronull.spawnarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.zeronull.spawnarena.ArenaState;
import me.zeronull.spawnarena.SpawnArena;

public class ArenaPlayerLeaveAreaEvent implements Listener {

    @EventHandler
    public void onPlayerTeleportOut(PlayerTeleportEvent event) {
        Player player = (Player) event.getPlayer();

        if(!SpawnArena.arena.isFighter(player)) {
            return;
        }

        if(SpawnArena.arena.getState() == ArenaState.INITALIZING || SpawnArena.arena.getState() == ArenaState.ENDING) {
            return;
        }

        if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN) {
            SpawnArena.arena.announceWinner(player);
            SpawnArena.arena.endFight();
        }
    }
    
}

