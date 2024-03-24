package me.zeronull.spawnarena;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

public abstract class Arena extends ArenaOptions {
    public static final String SPAWN_WORLD = "SpawnWorld";
    public ArenaQueue queue;
    protected String arenaName;
    protected Location spawnPoint1;
    protected Location spawnPoint2;
    private List<Fight> fights = new ArrayList<>();
    public Arena(String arenaName) {
        this.arenaName = arenaName;
    }

    public void setSpawnPoint1(final Location spawnPoint1) {
        this.spawnPoint1 = spawnPoint1;
    }

    public void setSpawnPoint2(final Location spawnPoint2) {
        this.spawnPoint2 = spawnPoint2;
    }

    public void addFight(Fight fight) {
        this.fights.add(fight);
    }

    public void removeFight(Fight fight) {
        this.fights.remove(fight);
    }

    public List<Fight> getFights() {
        return this.fights;
    }

    public Fight getFight(final Player p) {
        Fight found = null;

        for (final Fight fight : this.getFights()) {
            if (!fight.isFighter(p))
                continue;

            found = fight;
            break;
        }

        return found;
    }

    public List<Player> getOtherPlayers(final Fight fight) {
        final List<Player> players = new ArrayList<>();

        for (final Fight f : this.fights) {
            if (f.equals(fight))
                continue;

            players.addAll(f.getFighters());
        }

        return players;
    }

    public void clearFights() {
        this.fights.clear();
    }

    public void teleportFighters(Player fighter1, Player fighter2) {
        this.dismount(fighter1);
        fighter1.teleport(this.spawnPoint1);
        this.dismount(fighter2);
        fighter2.teleport(this.spawnPoint2);
    }

    private void dismount(final Player player) {
        final Entity vehicle = player.getVehicle();

        if (vehicle == null)
            return;

        vehicle.eject();
    }

    public void setQueue(ArenaQueue queue) {
        this.queue = queue;
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public boolean isSetUp() {
        return this.spawnPoint1 != null && this.spawnPoint2 != null;
    }

    /**
     * Convert Arena class to any of its subtypes
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T to(final Class<?> clazz) {
        if (!clazz.isInstance(this))
            throw new IllegalArgumentException("Arena is not of type " + clazz.getSimpleName());

        return (T) clazz.cast(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Arena))
            return false;

        final Arena arena = (Arena) object;
        return arena.getArenaName().equals(this.getArenaName());
    }

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
            if (!WORLD_EDIT_SUPPORT)
                return;

//            System.out.println(String.format("Vanished: %s", isVanished(p)));
            if (isVanished(p))
                return;

//            System.out.println(String.format("Spectator: %s", p.getGameMode() == GameMode.SPECTATOR));
            if (p.getGameMode() == GameMode.SPECTATOR)
                return;

//            System.out.println(String.format("is Fighter: %s", SpawnArena.arenas.hasFighter(p)));
            if (SpawnArena.arenas.hasFighter(p))
                return;

            final Location loc = p.getLocation();
            final Arena arena = SpawnArena.arenas.of(loc);

//            System.out.println(String.format("Arena is null: %s", arena == null));

            if (arena != null)
                p.performCommand(String.format("warp %s", arena.getArenaName()));
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

}
