package me.zeronull.spawnarena;

import me.zeronull.spawnarena.commands.JoinArenaQueueCommand;
import me.zeronull.spawnarena.commands.SetArenaSpawnCommand;
import me.zeronull.spawnarena.events.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnArena extends JavaPlugin {
    public static Arena arena;

    private void registerEvents() {
        if (Arena.ArenaUtils.WORLD_EDIT_SUPPORT)
            Bukkit.getServer().getPluginManager().registerEvents(new ArenaWorldguardPvPEvent(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDeathEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDropItemEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPreventCraftingSlot(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeave(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeaveAreaEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QueuePlayerLeave(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerLeaveSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerJoinEvent(), this);
    }

    private void registerCommands() {
        this.getCommand("joinarenaqueue").setExecutor(new JoinArenaQueueCommand());
        this.getCommand("setarenaspawn").setExecutor(new SetArenaSpawnCommand());
    }

    public void onEnable() {
        ConfigHandler.getInstance().updateCustomConfig();

        registerEvents();
        registerCommands();

        arena = ConfigHandler.getInstance().registerArenaFromConfig();
    }

    public void onDisable() {
        arena.getFight().endFight();
    }
}
