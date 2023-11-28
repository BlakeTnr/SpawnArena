package me.zeronull.spawnarena;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import org.bukkit.Location;

import java.io.File;
import java.nio.file.Path;

public class ConfigHandler {
    private static ConfigHandler config;
    private static GameConfig gameConfig;

    private ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if(!(config instanceof ConfigHandler)) {
            config = new ConfigHandler();
        }

        return config;
    }

    public void saveCustomConfig(GameConfig config) {
        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);
        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();

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

        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);
        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();

        YamlConfigurations.update(
                configFile,
                GameConfig.class,
                properties
        );
    }

    public GameConfig getCustomConfig() {
        if (gameConfig != null)
            return gameConfig;

        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);
        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();

        GameConfig config = YamlConfigurations.load(
                configFile,
                GameConfig.class,
                properties
        );

        return gameConfig = config;
    }

    protected Arena registerArenaFromConfig() {
        GameConfig gameConfig = this.getCustomConfig();
        Arena arena = new Arena(gameConfig.spawnPoint1, gameConfig.spawnPoint2);
        ArenaQueue queue = ArenaQueue.getInstance();
        queue.setArena(arena);
        arena.setQueue(queue);
        return arena;
    }

    public void setSpawnpoint(Location location, int spawnPointNumber) {
        GameConfig config = ConfigHandler.getInstance().getCustomConfig();
        if(spawnPointNumber == 1) {
            config.spawnPoint1 = location;
        } else if(spawnPointNumber == 2) {
            config.spawnPoint2 = location;
        }
        ConfigHandler.getInstance().saveCustomConfig(config);
    }
    
}
