package me.zeronull.spawnarena;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public final class Arenas implements Iterable<Arena> {
    public final class ArenasIterator implements Iterator<Arena> {
        private int currentIndex;
        private final List<Arena> arenas;

        public ArenasIterator(final List<Arena> arenas) {
            this.currentIndex = -1;
            this.arenas = arenas;
        }

        @Override
        public boolean hasNext() {
            return this.currentIndex < this.arenas.size() - 1;
        }

        @Override
        public Arena next() {
            if (!hasNext())
                this.currentIndex = -1;

            return this.arenas.get(++this.currentIndex);
        }

        public int getCurrentIndex() {
            return this.currentIndex;
        }

        public List<Arena> toList() {
            return this.arenas;
        }
    }

    private final List<Arena> arenas;

    private Arenas(final List<Arena> arenas) {
        this.arenas = arenas;
    }

    public void add(final Arena arena) {
        this.arenas.add(arena);
    }

    public void remove(final Arena arena) {
        this.arenas.remove(arena);
    }

    public boolean contains(final Arena arena) {
        return this.arenas.contains(arena);
    }

    public boolean isEmpty() { return this.arenas.isEmpty(); }

    public boolean hasActiveFight() {
        return this.arenas.stream().anyMatch(arena -> arena.getFight().orElse(null) != null);
    }

    /**
     * Checks if fighter is contained with any of these arenas
     * @param fighter
     * @return
     */
    public boolean hasFighter(final Player fighter) {
        return this.of(fighter) != null;
//        return this.arenas.stream().anyMatch(arena -> arena.getFight() != null && arena.getFight().isFighter(fighter));
    }

    public boolean hasFighter(final Player fighter, final Arena exclusion) {
        return this.of(fighter, Arrays.asList(exclusion)) != null;
    }

    /**
     * Gets the arena of the fighter
     * @param fighter
     * @return
     */
    public Arena of(final Player fighter) {
        return this.of(fighter, new ArrayList<>());
    }

    /**
     * Gets the arena of the fighter
     * @param fighter
     * @param exclusions
     * @return
     */
    public Arena of(final Player fighter, final List<Arena> exclusions) {
        for (final Arena arena : this.arenas) {
            if (arena.getFight().orElse(null) == null)
                continue;

            if (!arena.getFight().orElse(null).isFighter(fighter))
                continue;

            if (!arenas.isEmpty() && exclusions.contains(arena))
                continue;

            return arena;
        }

        return null; // Unable to find arena
    }

    /**
     * Gets the arena of specified name
     * @param name
     * @return
     */
    public Arena of(final String name) {
        for (final Arena arena : this.arenas) {
            if (!arena.getArenaName().equals(name))
                continue;

            return arena;
        }

        return null; // Unable to find arena
    }

    /**
     * Attempts to get an Arena depending on whether a player is standing inside of it
     * Uses WorldGuard to figure it out
     * If there is no region with the same name as the arena name, it will not work
     * @param loc
     * @return
     */
    public Arena of(final Location loc) {
        final RegionQuery query = Arena.ArenaUtils.REGION_CONTAINER.createQuery();
        final ApplicableRegionSet regionSet = query.getApplicableRegions(BukkitAdapter.adapt(loc));

//        System.out.println(String.format("Region set size: %s", regionSet.size()));

        for (final Arena arena : this) {
//            System.out.println(arena.getArenaName());

            for (final ProtectedRegion region : regionSet) {
//                System.out.println(region.getId() + " : " + arena.getArenaName());
                if (!region.getId().equals(arena.getArenaName()))
                    continue;

                return arena;
            }
        }

        return null;
    }

    /**
     * Get an Arena that the player is queued for
     * @param player
     * @return
     */
    public Arena fromQueued(final Player player) {
        return ArenaQueue.PLAYER_QUEUE_MAP.get(player).getArena();
    }

    /**
     * Checks if player is in any of the queues for any of the arenas
     * @param p
     * @return
     */
    public boolean isInQueue(final Player p) {
        return this.isInQueue(p, null);
    }

    public boolean isInQueue(final Player p, final ArenaQueue exclude) {
        return this.arenas.stream().filter(arena -> arena.queue != exclude).anyMatch(arena -> arena.queue.playerInQueue(p));
    }

    /**
     * Check if Arena exists
     * @param name
     * @return
     */
    public boolean exists(final String name) {
        return this.of(name) != null;
    }

    public List<Arena> toList() {
        return this.arenas;
    }

    public static Arenas fromList(final List<Arena> arenas) {
        return new Arenas(arenas);
    }

    @Override
    public Iterator<Arena> iterator() {
        return new ArenasIterator(this.arenas);
    }
}