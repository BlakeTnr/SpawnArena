package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.projectiles.ProjectileSource;

public final class ArenaPotionSplashEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplash(final PotionSplashEvent e) {
        final ThrownPotion potion = e.getEntity();
        final ProjectileSource shooterEntity = potion.getShooter();

        if (!(shooterEntity instanceof Player))
            return;

        final Player shooter = (Player) shooterEntity;

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if (!SpawnArena.arenas.hasFighter(shooter))
            return;

        final Arena arena = SpawnArena.arenas.of(shooter);
        final Fight fight = arena.getFight(shooter);

        if (fight == null)
            return;

        for (final LivingEntity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player))
                continue;

            final Player player = (Player) entity;

            if (fight.getFighters().contains(player))
                continue;

            e.setIntensity(entity, 0D);
        }
    }
}