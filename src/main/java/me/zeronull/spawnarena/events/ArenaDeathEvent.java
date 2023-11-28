package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class ArenaDeathEvent implements Listener {
    public static final Material BYPASS_ITEM = Material.TOTEM_OF_UNDYING;

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player defender = (Player) event.getEntity();

        if(!(SpawnArena.arena.getFight() instanceof Fight)) {
            return;
        }
        
        if(!(SpawnArena.arena.getFight().isFighter(defender))) {
            return;
        }

        if(!((defender.getHealth() - event.getFinalDamage()) <= 0)) {
            return;
        }

        if(defender.getInventory().getItemInOffHand().getType() == BYPASS_ITEM || defender.getInventory().getItemInMainHand().getType() == BYPASS_ITEM) {
            return;
        }

        event.setCancelled(true);
        SpawnArena.arena.getFight().announceWinner(defender);
        SpawnArena.arena.getFight().endFight();
    }
}
