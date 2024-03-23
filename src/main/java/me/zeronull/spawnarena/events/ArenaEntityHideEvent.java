package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

public final class ArenaEntityHideEvent implements Listener {
    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent e) {
        final Projectile projectile = e.getEntity();
        final ProjectileSource shooterEntity = projectile.getShooter();

        if (shooterEntity == null)
            return;

        if (!(shooterEntity instanceof Player))
            return;

        final Player shooter = (Player) shooterEntity;
        final Arena arena = SpawnArena.arenas.of(shooter);

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if (!SpawnArena.arenas.hasFighter(shooter))
            return;

        final Fight fight = arena.getFight(shooter);

        if (fight == null)
            return;

        this.handleHideEntity(fight, projectile);
    }

    private void handleHideEntity(final Fight fight, final Entity entity) {
        final Arena arena = fight.getArena();

        final List<Player> playersToHideEntityTo = arena.getOtherPlayers(fight);
        for (final Player player : playersToHideEntityTo) {
            if (player == null || !player.isOnline())
                continue;

            player.hideEntity(SpawnArena.INSTANCE, entity);
        }

//        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY); // WTF WAS I SMOKING???
//
//        packet.getModifier().write(0, new IntArrayList(new int[] { entity.getEntityId() }));
//
//        for (final Player player : playersToHideEntityTo) {
//            if (player == null || !player.isOnline())
//                continue;
//
//            try {
//                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
//            } catch (final Exception exception) {
//                exception.printStackTrace();
//            }
//        }
    }
}