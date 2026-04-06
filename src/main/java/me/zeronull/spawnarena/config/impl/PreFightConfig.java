package me.zeronull.spawnarena.config.impl;

import de.exlll.configlib.Comment;
import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.YamlConfigurationProperties;
import me.zeronull.spawnarena.PlayerPreFightData;
import me.zeronull.spawnarena.PlayerPreFightDataSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public final class PreFightConfig {
    public static final YamlConfigurationProperties PROPERTIES = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
            .addSerializer(PlayerPreFightData.class, new PlayerPreFightDataSerializer())
            .build();

    @Comment("Map of UUID -> PreFightData")
    public Map<UUID, PlayerPreFightData> map = new HashMap<>();
}