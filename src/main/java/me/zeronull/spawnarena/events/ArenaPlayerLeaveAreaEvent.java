package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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

        final Material type = event.getTo().clone().add(0, 0.5, 0).getBlock().getType();
        if (event.getCause() == TeleportCause.ENDER_PEARL && type.isSolid()) {
            event.setCancelled(true);
            return;
        }

        final Arena to = SpawnArena.arenas.of(event.getTo());
        if (to != null && to.equals(arena))
            return;

        if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN) {
            fight.announceWinner(player);
            fight.endFight();
        }
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER)
            return;

        final Player p = (Player) e.getEntity();

        if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION)
            return;

        if(!SpawnArena.arenas.hasActiveFight())
            return;

        if(!SpawnArena.arenas.hasFighter(p))
            return;

        final Arena arena = SpawnArena.arenas.of(p);
        final Fight fight = arena.getFight().get();

        if(fight.getState() == FightState.ENDING || fight.getState() == FightState.INITALIZING)
            return;

        final Arena to = SpawnArena.arenas.of(p.getLocation());
        if (to != null && to.equals(arena))
            return;

        fight.teleportToSpawn(p);
    }
    
}

