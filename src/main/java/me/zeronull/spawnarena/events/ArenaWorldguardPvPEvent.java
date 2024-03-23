package me.zeronull.spawnarena.events;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaWorldguardPvPEvent implements Listener {
    @EventHandler
    public void allowPlayerPVP(DisallowedPVPEvent event) {
        if(!(event.getAttacker() instanceof Player) || !(event.getDefender() instanceof Player)) {
            return;
        }

        Player damager = event.getAttacker();
        Player defender = event.getDefender();

//        System.out.println(!SpawnArena.arenas.hasActiveFight());
//        System.out.println(!SpawnArena.arenas.hasFighter(damager));
//        System.out.println(!SpawnArena.arenas.hasFighter(defender));

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if (!SpawnArena.arenas.hasFighter(damager) || !SpawnArena.arenas.hasFighter(defender))
            return;

        final Arena arena = SpawnArena.arenas.of(damager);

        if (arena.getFight(damager) != null && arena.getFight(damager).getState() == FightState.INITALIZING)
            return;

        if (!arena.equals(SpawnArena.arenas.of(damager.getLocation())))
            return;

//        if(SpawnArena.arena.getFight().getState() == FightState.INITALIZING) {
//            return;
//        }

        event.setCancelled(true);
    }
}
