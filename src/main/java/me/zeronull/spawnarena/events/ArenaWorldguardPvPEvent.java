package me.zeronull.spawnarena.events;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import me.zeronull.spawnarena.Fight;
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
