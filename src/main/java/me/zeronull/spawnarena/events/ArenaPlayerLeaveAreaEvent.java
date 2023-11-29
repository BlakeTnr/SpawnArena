package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class ArenaPlayerLeaveAreaEvent implements Listener {

    @EventHandler
    public void onPlayerTeleportOut(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if(!SpawnArena.arenas.hasActiveFight()) {
            return;
        }

        if(!SpawnArena.arenas.hasFighter(player)) {
            return;
        }

        final Arena arena = SpawnArena.arenas.of(player);
        final Fight fight = arena.getFight().get();

        if(fight.getState() == FightState.ENDING || fight.getState() == FightState.INITALIZING) {
            return;
        }

        if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN) {
            fight.announceWinner(player);
            fight.endFight();
        }
    }
    
}

