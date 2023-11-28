package me.zeronull.spawnarena;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class Arena implements Listener {
    public static final class ArenaUtils {
        public static final boolean WORLD_EDIT_SUPPORT;
        public static final WorldGuard WORLD_GUARD_INSTANCE;
        public static final RegionContainer REGION_CONTAINER;

        static {
            boolean worldEditSupport;

            try {
                Class.forName("com.sk89q.worldguard.WorldGuard");
                worldEditSupport = true;
            } catch (final ClassNotFoundException exception) {
                worldEditSupport = false;
            }

            WORLD_EDIT_SUPPORT = worldEditSupport;

            WORLD_GUARD_INSTANCE = !WORLD_EDIT_SUPPORT ? null : WorldGuard.getInstance();
            REGION_CONTAINER = !WORLD_EDIT_SUPPORT ? null : WORLD_GUARD_INSTANCE.getPlatform().getRegionContainer();
        }

        public static void kickOutLingeringPlayers() {
            for (final Player p : Bukkit.getOnlinePlayers())
                kickOutLingeringPlayer(p);
        }

        public static void kickOutLingeringPlayer(final Player p) {
            final String regionName = ConfigHandler.getInstance().getCustomConfig().arenaRegion;

            if (!WORLD_EDIT_SUPPORT)
                return;

            if (isVanished(p))
                return;

            if (p.getGameMode() == GameMode.SPECTATOR)
                return;

            if (SpawnArena.arena.getFight() != null && SpawnArena.arena.getFight().isFighter(p))
                return;

            final RegionQuery query = REGION_CONTAINER.createQuery();
            final ApplicableRegionSet regionSet = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));

            for (final ProtectedRegion region : regionSet) {
                if (!region.getId().equals(regionName))
                    continue;

                p.performCommand(ConfigHandler.getInstance().getCustomConfig().warpCommand);
                break;
            }
        }

        /**
         * Checking if the player is vanished on SuperVanish/PremiumVanish
         *
         * @param player
         * @return
         */
        public static boolean isVanished(final Player player) {
            final List<MetadataValue> values = player.getMetadata("vanished");

//        if (values.isEmpty())
//            return SpigotCore.INSTANCE.getWs().getPlayerStateMap().get(player.getUniqueId()).isVanished();

            for (final MetadataValue meta : values)
                if (meta.asBoolean())
                    return true;

            return false;
        }
    }

    public ArenaQueue queue;
    ArenaState arenaState = ArenaState.EMPTY;
    Location spawnPoint1;
    Location spawnPoint2;
    private Fight fight;

    public Arena(Location spawnPoint1, Location spawnPoint2) {
        this.spawnPoint1 = spawnPoint1;
        this.spawnPoint2 = spawnPoint2;
    }

    public ArenaState getState() {
        return arenaState;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
        this.arenaState = ArenaState.IN_FIGHT;
    }

    public Fight getFight() {
        return fight;
    }

    public void clearFight() {
        this.fight = null;
    }

    public void teleportFighters(Player fighter1, Player fighter2) {
        fighter1.teleport(this.spawnPoint1);
        fighter2.teleport(this.spawnPoint2);
    }

    public void setQueue(ArenaQueue queue) {
        this.queue = queue;
    }
    
}
