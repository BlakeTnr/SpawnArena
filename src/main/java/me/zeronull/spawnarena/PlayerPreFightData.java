package me.zeronull.spawnarena;

import me.zeronull.spawnarena.inventory.BukkitSerialization;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class PlayerPreFightData {
    Player player;
    String username;
    ItemStack[] contents;
    ItemStack[] armorContents;
    int level;
    float exp;
    Location previousLocation;

    public PlayerPreFightData(Player player) {
        this.player = player;
        this.username = this.player.getName();
        contents = cloneItemStackArray(player.getInventory().getContents());
        armorContents = cloneItemStackArray(player.getInventory().getArmorContents());
        level = player.getLevel();
        exp = player.getExp();
        previousLocation = player.getLocation();
    }

    /**
     * Deserialize PlayerPreFightData from a JSONObject
     * @param obj
     * @throws IOException
     */
    public PlayerPreFightData(final JSONObject obj) throws IOException {
        this.player = Bukkit.getPlayer(UUID.fromString(obj.getString("uuid")));
        this.username = obj.getString("username");
        this.contents = BukkitSerialization.itemStackArrayFromBase64(obj.getString("contents"));
        this.armorContents = BukkitSerialization.itemStackArrayFromBase64(obj.getString("armor_contents"));
        this.level = obj.getInt("level");
        this.exp = obj.getFloat("exp");
        this.previousLocation = BukkitSerialization.locationFromJson(new JSONObject(obj.getString("previous_location")));
    }

    public void restore() {
        if (this.player == null || !this.player.isOnline())
            throw new IllegalStateException(String.format("Failed to restore PlayerPreFightData for %s because they were offline", this.username));

        // These 2 just assume they were at this before
        this.player.setHealth(20);
        this.player.setFireTicks(0);

        this.player.closeInventory();
        this.player.teleport(this.previousLocation);
        this.player.setLevel(this.level);
        this.player.setExp(this.exp);
        this.player.getInventory().setContents(this.contents);
        this.player.getInventory().setArmorContents(this.armorContents);
    }

    private ItemStack[] cloneItemStackArray(ItemStack[] items) {
        ItemStack[] newArray = new ItemStack[items.length];
        for(int i=0; i<items.length; i++) {
            try{
                newArray[i] = items[i].clone();
            } catch(NullPointerException e) {
                continue;
            }
        }
        return newArray;
    }

    public JSONObject toJsonObject() {
        final JSONObject obj = new JSONObject();

        obj.put("uuid", this.player.getUniqueId().toString());
        obj.put("username", this.username);
        obj.put("contents", BukkitSerialization.itemStackArrayToBase64(this.contents));
        obj.put("armor_contents", BukkitSerialization.itemStackArrayToBase64(this.armorContents));
        obj.put("level", this.level);
        obj.put("exp", this.exp);
        obj.put("previous_location", BukkitSerialization.locationToJson(this.previousLocation));

        return obj;
    }
}
