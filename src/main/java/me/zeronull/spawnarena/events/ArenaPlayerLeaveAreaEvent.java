package me.zeronull.spawnarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.zeronull.spawnarena.ArenaState;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;

public class ArenaPlayerLeaveAreaEvent implements Listener {

    @EventHandler
    public void onPlayerTeleportOut(PlayerTeleportEvent event) {
        Player player = (Player) event.getPlayer();

        if(!(SpawnArena.arena.getFight() instanceof Fight)) {
            return;
        }

        if(!SpawnArena.arena.getFight().isFighter(player)) {
            return;
        }

        if(SpawnArena.arena.getFight().getState() == FightState.ENDING || SpawnArena.arena.getFight().getState() == FightState.INITALIZING) {
            return;
        }

        if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN) {
            SpawnArena.arena.getFight().announceWinner(player);
            SpawnArena.arena.getFight().endFight();
        }
    }
    
}

