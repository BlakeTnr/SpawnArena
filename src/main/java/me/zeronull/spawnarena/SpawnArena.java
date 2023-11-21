package me.zeronull.spawnarena;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.zeronull.spawnarena.commands.JoinArenaQueueCommand;
import me.zeronull.spawnarena.commands.SetArenaSpawnCommand;
import me.zeronull.spawnarena.events.ArenaCommandEvent;
import me.zeronull.spawnarena.events.ArenaDeathEvent;
import me.zeronull.spawnarena.events.ArenaDropItemEvent;
import me.zeronull.spawnarena.events.ArenaPlayerLeave;
import me.zeronull.spawnarena.events.ArenaPlayerLeaveAreaEvent;
import me.zeronull.spawnarena.events.ArenaPreventCraftingSlot;
import me.zeronull.spawnarena.events.ArenaWorldguardPvPEvent;
import me.zeronull.spawnarena.events.QueuePlayerLeave;

public class SpawnArena extends JavaPlugin {
    public static Arena arena;

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaWorldguardPvPEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDeathEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDropItemEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPreventCraftingSlot(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeave(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeaveAreaEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QueuePlayerLeave(), this);
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
        arena.endFight();
    }
}
