package me.zeronull.spawnarena.config;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.ArenaQueue;
import me.zeronull.spawnarena.SavableArena;
import me.zeronull.spawnarena.config.impl.ArenaConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private static ConfigHandler config;

    private ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if(!(config instanceof ConfigHandler)) {
            config = new ConfigHandler();
        }

        return config;
    }

//    public void saveCustomConfig(GameConfig config) {
//        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
//                .build();
//
//        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);
//        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();
//
//        YamlConfigurations.save(
//                configFile,
//                GameConfig.class,
//                config,
//                properties
//        );
//    }
//
//    public void updateCustomConfig() {
//        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
//                .build();
//
//        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);
//        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();
//
//        YamlConfigurations.update(
//                configFile,
//                GameConfig.class,
//                properties
//        );
//    }
//
//    public GameConfig getCustomConfig() {
//        YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
//                .build();
//
//        SpawnArena plugin = SpawnArena.getPlugin(SpawnArena.class);
//        Path configFile = new File(plugin.getDataFolder(), "config.yml").toPath();
//
//        GameConfig config = YamlConfigurations.load(
//                configFile,
//                GameConfig.class,
//                properties
//        );
//
//        return config;
//    }

    public List<Arena> registerArenasFromConfig() {
        final List<Arena> arenas = new ArrayList<>();
        final File[] arenaConfigs = SavableArena.ARENAS_DIR.listFiles();

        if (arenaConfigs == null || arenaConfigs.length == 0)
            return new ArrayList<>();

        for (final File configFile : arenaConfigs) {
            final ArenaConfig config = SavableArena.getArenaConfig(configFile);
            final Arena arena = SavableArena.deserialize(config);

            final ArenaQueue queue = new ArenaQueue(arena);
            arena.setQueue(queue);

            arena.setShouldClearItems(config.shouldClearItems);
            arena.setAllowDamage(config.allowDamage);
            arena.setAllowPvp(config.allowPvp);
            arena.setDeathOnTouchLiquid(config.deathOnTouchLiquid);
            arena.setWinOnStepStonePressurePlate(config.winOnStepStonePressurePlate);
            arena.setGiveKnockBackStick(config.giveKnockBackStick);

            arenas.add(arena);
        }

        return arenas;
    }

//    public void setSpawnpoint(Location location, int spawnPointNumber) {
//        GameConfig config = ConfigHandler.getInstance().getCustomConfig();
//        if(spawnPointNumber == 1) {
//            config.spawnPoint1 = location;
//        } else if(spawnPointNumber == 2) {
//            config.spawnPoint2 = location;
//        }
//        ConfigHandler.getInstance().saveCustomConfig(config);
//    }
    
}
