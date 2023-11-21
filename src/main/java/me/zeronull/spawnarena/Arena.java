package me.zeronull.spawnarena;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Arena implements Listener {
    // Todo: Refactor this into an Arena & Fight class
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
