package me.zeronull.spawnarena;

import me.zeronull.spawnarena.config.ConfigHandler;
import me.zeronull.spawnarena.config.impl.PreFightConfig;
import me.zeronull.spawnarena.events.ArenaPlayerConsumeEvent;
import me.zeronull.spawnarena.inventory.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PlayerPreFightData {
    public Player player;

    protected String username;
    protected ItemStack[] contents;
    protected ItemStack[] armorContents;
    protected int level;
    protected float exp;
    protected Location previousLocation;
    protected GameMode previousGameMode;
    protected double health;
    protected int foodLevel;
    protected List<PotionEffect> effects;

    public PlayerPreFightData(Player player) {
        this.player = player;
        this.username = this.player.getName();
        contents = cloneItemStackArray(player.getInventory().getContents());
        armorContents = cloneItemStackArray(player.getInventory().getArmorContents());
        level = player.getLevel();
        exp = player.getExp();
        previousLocation = player.getLocation();
        previousGameMode = player.getGameMode();
        health = player.getHealth();
        foodLevel = player.getFoodLevel();
        effects = new ArrayList<>(player.getActivePotionEffects());

        final ConfigHandler handler = ConfigHandler.getInstance();

        final File configFile = new File(SpawnArena.INSTANCE.getDataFolder(), "prefights.yml");

        if (!configFile.exists())
            handler.updatePreFightConfig();

        final PreFightConfig config = handler.getPreFightConfig();

        config.map.put(player.getUniqueId(), this);
        handler.savePreFightConfig(config);
    }

    /**
     * Deserialize PlayerPreFightData from a JSONObject
     *
     * @param obj
     * @throws IOException
     */
    public PlayerPreFightData(final JSONObject obj) throws Exception {
        this.player = Bukkit.getPlayer(UUID.fromString(obj.getString("uuid")));
        this.username = obj.getString("username");
        this.contents = ItemSerializer.deserialize(obj.getString("contents"));
        this.armorContents = ItemSerializer.deserialize(obj.getString("armor_contents"));
        this.level = obj.getInt("level");
        this.exp = obj.getFloat("exp");
        this.previousLocation = LocationUtils.locationFromJson(new JSONObject(obj.getString("previous_location")));
        this.previousGameMode = GameMode.valueOf(obj.getString("previous_gamemode"));
        this.health = obj.getDouble("health");
        this.foodLevel = obj.getInt("food_level");
    }

    public void restore() {
        if (this.player == null || !this.player.isOnline())
            throw new IllegalStateException(String.format("Failed to restore PlayerPreFightData for %s because they were offline", this.username));

        ArenaPlayerConsumeEvent.PLAYER_FINISH_GAME_MAP.put(this.player.getUniqueId(), Instant.now().getEpochSecond());

        this.player.closeInventory();
        FoliaUtils.teleport(this.player, this.previousLocation);

        // These 2 just assume they were at this before
        this.player.setFireTicks(0);

        this.player.setHealth(this.health);
        this.player.setFoodLevel(this.foodLevel);
        this.player.setLevel(this.level);
        this.player.setExp(this.exp);
        this.player.getInventory().setContents(this.contents);
        this.player.getInventory().setArmorContents(this.armorContents);
        this.player.setGameMode(this.previousGameMode);

        this.restorePotionEffects();

        final ConfigHandler handler = ConfigHandler.getInstance();
        final PreFightConfig config = handler.getPreFightConfig();

        config.map.remove(player.getUniqueId());
        handler.savePreFightConfig(config);
    }

    private void restorePotionEffects() {
        if (this.effects == null || this.effects.isEmpty())
            return;

        for (final PotionEffect effect : this.player.getActivePotionEffects())
            this.player.removePotionEffect(effect.getType());

        for (final PotionEffect effect : this.effects)
            this.player.addPotionEffect(effect);
    }

    private ItemStack[] cloneItemStackArray(ItemStack[] items) {
        ItemStack[] newArray = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            try {
                newArray[i] = items[i].clone();
            } catch (NullPointerException e) {
                continue;
            }
        }
        return newArray;
    }

    public JSONObject toJsonObject() throws Exception {
        final JSONObject obj = new JSONObject();

        obj.put("uuid", this.player.getUniqueId().toString());
        obj.put("username", this.username);
        obj.put("contents", ItemSerializer.serialize(this.contents));
        obj.put("armor_contents", ItemSerializer.serialize(this.armorContents));
        obj.put("level", this.level);
        obj.put("exp", this.exp);
        obj.put("previous_location", LocationUtils.locationToJson(this.previousLocation).toString());
        obj.put("previous_gamemode", this.previousGameMode.name());
        obj.put("health", this.health);
        obj.put("food_level", this.foodLevel);

        return obj;
    }
}
