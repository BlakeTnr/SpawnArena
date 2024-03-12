package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ArenaProjectileDupeFix implements Listener {
    private final List<Projectile> thrownProjectiles = new ArrayList<>();

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent e) {
        final Entity entity = e.getEntity();

        if (entity.getType() != EntityType.TRIDENT && !entity.getType().name().contains("ARROW"))
            return;

        final Projectile projectile = (Projectile) entity;
        final ProjectileSource shooterEntity = projectile.getShooter();

        if (shooterEntity == null)
            return;

        if (!(shooterEntity instanceof Player))
            return;

        final Player shooter = (Player) shooterEntity;
        final Arena arena = SpawnArena.arenas.of(shooter);

        if (arena != null && arena.getFight().get().getState() != FightState.IN_FIGHT) {
            e.setCancelled(true);
            return;
        }

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if (!SpawnArena.arenas.hasFighter(shooter))
            return;

        if (!this.thrownProjectiles.contains(projectile))
            this.thrownProjectiles.add(projectile);
    }

    @EventHandler
    public void onPlayerPickupArrow(final PlayerPickupArrowEvent e) {
        final AbstractArrow arrow = e.getArrow();

        if (!this.thrownProjectiles.contains(arrow))
            return;

        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();

        final ProjectileSource source = arrow.getShooter();

        if (source instanceof Player) {
            final Player shooter = (Player) source;
            final Arena arena = SpawnArena.arenas.of(shooter);

            if (arena != null && arena.getFight().isPresent()) {
                final Fight fight = arena.getFight().orElse(null);

                if (fight.getState() == FightState.IN_FIGHT && !fight.isFighter(p))
                    e.setCancelled(true);
            }
        }

        final long now = Instant.now().getEpochSecond();
        final long then = ArenaPlayerConsumeEvent.PLAYER_FINISH_GAME_MAP.getOrDefault(uuid, 0L);
        final long diff = now - then;

        final boolean before5SecondsAgo = diff < 5;

        if (!before5SecondsAgo)
            return;

        e.setCancelled(true);
        arrow.remove();
    }
}