package me.zeronull.spawnarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;

import me.zeronull.spawnarena.ArenaState;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;

public class ArenaWorldguardPvPEvent implements Listener {
    @EventHandler
    public void allowPlayerPVP(DisallowedPVPEvent event) {
        if(!(event.getAttacker() instanceof Player) || !(event.getDefender() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getAttacker();
        Player defender = (Player) event.getDefender();

        if(!(SpawnArena.arena.getFight() instanceof Fight)) {
            return;
        }

        if(SpawnArena.arena.getFight().getState() == FightState.INITALIZING) {
            return;
        }

        if(SpawnArena.arena.getFight().isFighter(damager) && SpawnArena.arena.getFight().isFighter(defender)) {
            event.setCancelled(true);
        }
    }
}
