package me.zeronull.spawnarena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;

public class ArenaWorldguardPvPEvent implements Listener {
    @EventHandler
    public void allowPlayerPVP(DisallowedPVPEvent event) {
        if(!(event.getAttacker() instanceof Player) || !(event.getDefender() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getAttacker();
        Player defender = (Player) event.getDefender();

        if(SpawnArena.arena.isInitializing) {
            return;
        }

        if(SpawnArena.arena.isFighter(damager) && SpawnArena.arena.isFighter(defender)) {
            event.setCancelled(true);
        }
    }
}
