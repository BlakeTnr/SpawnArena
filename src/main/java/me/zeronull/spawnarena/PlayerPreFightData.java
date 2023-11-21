package me.zeronull.spawnarena;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerPreFightData {
    Player player;
    ItemStack[] contents;
    ItemStack[] armorContents;
    int level;
    float exp;
    Location previousLocation;

    public PlayerPreFightData(Player player) {
        this.player = player;
        contents = cloneItemStackArray(player.getInventory().getContents());
        armorContents = cloneItemStackArray(player.getInventory().getArmorContents());
        level = player.getLevel();
        exp = player.getExp();
        previousLocation = player.getLocation();
    }

    public void restore() {
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
}
