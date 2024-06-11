package me.zeronull.spawnarena.events;

import com.booksaw.betterTeams.customEvents.TeamDisallowedPvPEvent;
import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ArenaTeamPvPEvent implements Listener {
    @EventHandler
    public void teamDisallowedPvP(final TeamDisallowedPvPEvent e) {
        final Player damager = e.getSource();

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if (!SpawnArena.arenas.hasFighter(damager))
            return;

        final Arena arena = SpawnArena.arenas.of(damager);

        if (arena.getFight(damager) != null && arena.getFight(damager).getState() == FightState.COUNTDOWN)
            return;

        if (!arena.equals(SpawnArena.arenas.of(damager.getLocation())))
            return;

        e.setCancelled(true);
    }
}