package me.zeronull.spawnarena;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.Location;

@Configuration
public final class GameConfig {
    @Comment("Spawnpoint for 1st fighter")
    public Location spawnPoint1;

    @Comment("Spawnpoint for 2nd fighter")
    public Location spawnPoint2;

    @Comment("WorldGuard region for the arena")
    public String arenaRegion = "arena";

    @Comment("Command that should be executed by players lingering in the arena")
    public String warpCommand = "warp arena";
}