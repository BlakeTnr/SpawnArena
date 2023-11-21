package me.zeronull.spawnarena;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ArenaDeathEvent implements Listener {
    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player defender = (Player) event.getEntity();
        
        if(!(SpawnArena.arena.isFighter(defender))) {
            return;
        }

        if(!((defender.getHealth() - event.getFinalDamage()) <= 0)) {
            return;
        }

        if(defender.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
            return;
        }

        event.setCancelled(true);
        SpawnArena.arena.announceWinner(defender);
        SpawnArena.arena.endFight();
    }
}
