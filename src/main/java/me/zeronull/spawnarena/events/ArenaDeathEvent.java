package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
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
        if(!(event.getEntity() instanceof Player defender)) {
            return;
        }

        if (!SpawnArena.arenas.hasActiveFight()) {
            return;
        }
        
        if(!(SpawnArena.arenas.hasFighter(defender))) {
            return;
        }

        final Arena arena = SpawnArena.arenas.of(defender);

        if (!arena.isAllowDamage())
            event.setDamage(0D);

        if (!arena.isAllowPvp())
            event.setCancelled(true);

        if(!((defender.getHealth() - event.getFinalDamage()) <= 0)) {
            return;
        }

        if(defender.getInventory().getItemInOffHand().getType() == BYPASS_ITEM || defender.getInventory().getItemInMainHand().getType() == BYPASS_ITEM) {
            return;
        }

        event.setCancelled(true);

        arena.getFight().ifPresent(fight -> {
            fight.announceWinner(defender);
            fight.endFight();
        });
    }
}
