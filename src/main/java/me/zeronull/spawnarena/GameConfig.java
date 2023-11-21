package me.zeronull.spawnarena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public final class GameConfig {
    @Comment("Spawnpoint for 1st fighter")
    public Location spawnPoint1;
    @Comment("Spawnpoint for 2nd fighter")
    public Location spawnPoint2;
}