package me.zeronull.spawnarena;

import java.io.File;
import java.nio.file.Path;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import de.exlll.configlib.ConfigLib;

public class SpawnArena extends JavaPlugin {
    public static Arena arena;

    private void registerArenaFromConfig(GameConfig config) {
        arena = new Arena(config.spawnPoint1, config.spawnPoint2);
        ArenaQueue queue = ArenaQueue.getInstance();
        queue.setArena(arena);
        arena.setQueue(queue);
    }

    public GameConfig getCustomConfig() {
        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        Path configFile = new File(getDataFolder(), "config.yml").toPath();

        GameConfig config = YamlConfigurations.load(
                configFile,
                GameConfig.class,
                properties
        );

        return config;
    }

    public void saveCustomConfig(GameConfig config) {
        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        Path configFile = new File(getDataFolder(), "config.yml").toPath();

        YamlConfigurations.save(
                configFile,
                GameConfig.class,
                config,
                properties
        );
    }

    public void updateCustomConfig() {
        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        Path configFile = new File(getDataFolder(), "config.yml").toPath();

        YamlConfigurations.update(
                configFile,
                GameConfig.class,
                properties
        );
    }

    public void onEnable() {
        updateCustomConfig();

        Bukkit.getServer().getPluginManager().registerEvents(new ArenaWorldguardPvPEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDeathEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDropItemEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPreventCraftingSlot(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeave(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeaveAreaEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QueuePlayerLeave(), this);
        this.getCommand("joinarenaqueue").setExecutor(new JoinArenaQueueCommand());
        this.getCommand("setarenaspawn").setExecutor(new SetArenaSpawnCommand());

        registerArenaFromConfig(getCustomConfig());
    }

    public void onDisable() {
        arena.endFight();
    }
}
