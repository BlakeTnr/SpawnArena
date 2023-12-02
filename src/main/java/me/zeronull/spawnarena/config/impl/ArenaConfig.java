package me.zeronull.spawnarena.config.impl;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.Location;

@Configuration
public final class ArenaConfig {
    @Comment("Name of the arena")
    public String arenaName; // To keep everything simple, this should be the warp name and the region name in world guard

    @Comment("Spawnpoint for 1st fighter")
    public Location spawnPoint1;

    @Comment("Spawnpoint for 2nd fighter")
    public Location spawnPoint2;

    @Comment("Should it clear the inventory on arena start of fighters?")
    public boolean shouldClearItems = false;

    @Comment("Should fighters be able to deal damage to each other?")
    public boolean allowDamage = true;

    @Comment("Should fighters be able to pvp each other?")
    public boolean allowPvp = true;

    @Comment("Should a fighter die if they touch liquid?")
    public boolean deathOnTouchLiquid;

    @Comment("Should a fighter win against their opponent if they stand on a stone pressure plate?")
    public boolean winOnStepStonePressurePlate;

    @Comment("Should it give the fighters knockback stick when they are first teleported into the arena")
    public boolean giveKnockBackStick;
}