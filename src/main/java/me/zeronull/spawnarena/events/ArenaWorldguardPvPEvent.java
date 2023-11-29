package me.zeronull.spawnarena.events;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class ArenaWorldguardPvPEvent implements Listener {
    @EventHandler
    public void allowPlayerPVP(DisallowedPVPEvent event) {
        //Condition 'event.getAttacker() instanceof Player' is redundant and can be replaced with a null check
        //Condition 'event.getDefender() instanceof Player' is redundant and can be replaced with a null check
        if (event.getAttacker() == null || event.getDefender() == null) {
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

        Optional<Fight> optionalFight = arena.getFight();
        if (optionalFight.isEmpty()) {
            return;
        }

        if (optionalFight.get().getState() == FightState.INITALIZING)
            return;

//        if(SpawnArena.arena.getFight().getState() == FightState.INITALIZING) {
//            return;
//        }

        event.setCancelled(true);
    }
}
