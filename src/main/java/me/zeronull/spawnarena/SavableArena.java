package me.zeronull.spawnarena;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import me.zeronull.spawnarena.config.impl.ArenaConfig;
import org.bukkit.Location;

import java.io.File;
import java.util.function.Consumer;

public final class SavableArena extends Arena {
    public static final File ARENAS_DIR = new File(SpawnArena.getPlugin(SpawnArena.class).getDataFolder(), "arenas");
    private final String configName;

    public SavableArena(final String arenaName) {
        super(arenaName);
        this.configName = String.format("%s.yml", super.getArenaName());
    }

    @Override
    public void setSpawnPoint1(final Location spawnPoint1) {
        this.save(config -> config.spawnPoint1 = spawnPoint1);
        super.setSpawnPoint1(spawnPoint1);
    }

    @Override
    public void setSpawnPoint2(final Location spawnPoint2) {
        this.save(config -> config.spawnPoint2 = spawnPoint2);
        super.setSpawnPoint2(spawnPoint2);
    }

    @Override
    public ArenaOptions setShouldClearItems(final boolean value) {
        this.save(config -> config.shouldClearItems = value);
        return super.setShouldClearItems(value);
    }

    @Override
    public ArenaOptions setAllowDamage(final boolean value) {
        this.save(config -> config.allowDamage = value);
        return super.setAllowDamage(value);
    }

    @Override
    public ArenaOptions setAllowPvp(final boolean value) {
        this.save(config -> config.allowPvp = value);
        return super.setAllowPvp(value);
    }

    @Override
    public ArenaOptions setDeathOnTouchLiquid(final boolean value) {
        this.save(config -> config.deathOnTouchLiquid = value);
        return super.setDeathOnTouchLiquid(value);
    }

    @Override
    public ArenaOptions setWinOnStepStonePressurePlate(final boolean value) {
        this.save(config -> config.winOnStepStonePressurePlate = value);
        return super.setWinOnStepStonePressurePlate(value);
    }

    public File getConfigFile() {
        final File configFile = new File(ARENAS_DIR, this.configName);
        return configFile;
    }

    public void save(final Consumer<ArenaConfig> action) {
        final File configFile = this.getConfigFile();
        final ArenaConfig config = getArenaConfig(configFile);

        action.accept(config);
        saveArenaConfig(configFile, config);
    }

    public static SavableArena deserialize(final File configFile) {
        final ArenaConfig config = getArenaConfig(configFile);
        return deserialize(config);
    }

    public static SavableArena deserialize(final ArenaConfig config) {
        final SavableArena arena = new SavableArena(config.arenaName);

        arena.setSpawnPoint1(config.spawnPoint1);
        arena.setSpawnPoint2(config.spawnPoint2);

        arena.setShouldClearItems(config.shouldClearItems);
        arena.setAllowDamage(config.allowDamage);
        arena.setAllowPvp(config.allowPvp);
        arena.setDeathOnTouchLiquid(config.deathOnTouchLiquid);
        arena.setWinOnStepStonePressurePlate(config.winOnStepStonePressurePlate);

        return arena;
    }

    public static ArenaConfig getArenaConfig(final File configFile) {
        final YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        final ArenaConfig config = YamlConfigurations.load(
                configFile.toPath(),
                ArenaConfig.class,
                properties
        );

        return config;
    }

    public static void createArenaConfig(final File configFile) {
        final YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        YamlConfigurations.update(
                configFile.toPath(),
                ArenaConfig.class,
                properties
        );
    }

    public static void saveArenaConfig(final File configFile, final ArenaConfig config) {
        final YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .build();

        YamlConfigurations.save(
                configFile.toPath(),
                ArenaConfig.class,
                config,
                properties
        );
    }
}