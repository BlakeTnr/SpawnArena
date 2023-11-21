package me.zeronull.spawnarena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.google.common.io.ByteArrayDataOutput;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;

import net.md_5.bungee.api.chat.hover.content.Item;

public class Arena implements Listener {
    ArenaQueue queue;
    Location spawnPoint1;
    Location spawnPoint2;
    Location previousLocation1;
    Location previousLocation2;
    Player fighter1;
    Player fighter2;
    ItemStack[] contents1;
    ItemStack[] contents2;
    ItemStack[] armorContents1;
    ItemStack[] armorContents2;
    byte[] inventoryStream1;
    byte[] inventoryStream2;
    boolean isInitializing = false;
    boolean isEnding = false;
    
    
    int level1;
    int level2;
    float exp1;
    float exp2;

    public Arena(Location spawnPoint1, Location spawnPoint2) {
        this.spawnPoint1 = spawnPoint1;
        this.spawnPoint2 = spawnPoint2;
    }

    public void setQueue(ArenaQueue queue) {
        this.queue = queue;
    }

    public boolean bothFightersOnline() {
        return (fighter1.isOnline() && fighter2.isOnline());
    }
    
    public void initiateFight(Player fighter1, Player fighter2) {
        isInitializing = true;
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;

        Plugin plugin = SpawnArena.getPlugin(SpawnArena.class);
        new BukkitRunnable() {
            int counter = 5;

            @Override
            public void run() {
                if(!fighter1.isOnline()) {
                    String displayName = fighter1.getDisplayName();
                    fighter2.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + displayName + " has left... cancelling fight"));
                    cancelFight();
                    queue.tryStartFight();
                    this.cancel();
                    return;
                }

                if(!fighter2.isOnline()) {
                    String displayName = fighter2.getDisplayName();
                    fighter1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + displayName + " has left... cancelling fight"));
                    cancelFight();
                    queue.tryStartFight();
                    this.cancel();
                    return;
                }

                if(counter == 0) {
                    startFight();
                    isInitializing = false;
                    this.cancel();
                } else {
                    fighter1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Starting in " + counter + "..."));
                    fighter2.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Starting in " + counter + "..."));
                    counter--;
                }
            }
            
        }.runTaskTimer(plugin, 20L, 20L);
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

    public void startFight() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "invsave " + fighter1.getName());
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "invsave " + fighter2.getName());
        contents1 = cloneItemStackArray(fighter1.getInventory().getContents());
        contents2 = cloneItemStackArray(fighter2.getInventory().getContents());
        armorContents1 = cloneItemStackArray(fighter1.getInventory().getArmorContents());
        armorContents2 = cloneItemStackArray(fighter2.getInventory().getArmorContents());
        level1 = fighter1.getLevel();
        level2 = fighter2.getLevel();
        exp1 = fighter1.getExp();
        exp2 = fighter2.getExp();
        previousLocation1 = fighter1.getLocation();
        previousLocation2 = fighter2.getLocation();
        
        fighter1.teleport(spawnPoint1);
        fighter2.teleport(spawnPoint2);
    }

    public boolean isInUse() {
        return (fighter1 != null || fighter2 != null); // Todo: This should be &&
    }

    public void cancelFight() {
        isEnding = true;
        fighter1 = null;
        fighter2 = null;
        isEnding = false;
    }

    public void endFight() {
        isEnding = true;
        fighter1.closeInventory();
        fighter2.closeInventory();
        fighter1.teleport(previousLocation1);
        fighter2.teleport(previousLocation2);
        fighter1.setHealth(20);
        fighter2.setHealth(20);
        fighter1.setLevel(level1);
        fighter2.setLevel(level2);
        fighter1.setExp(exp1);
        fighter2.setExp(exp2);
        fighter1.setFireTicks(0);
        fighter2.setFireTicks(0);
        fighter1.getInventory().setContents(contents1);
        fighter1.getInventory().setArmorContents(armorContents1);
        fighter2.getInventory().setContents(contents2);
        fighter2.getInventory().setArmorContents(armorContents2);
        fighter1 = null;
        fighter2 = null;
        isEnding = false;

        this.queue.tryStartFight();
    }

    public void announceWinner(Player whoDied) {
        String winnerName;
        String loserName;
        if(fighter1.equals(whoDied)) {
            winnerName = fighter2.getDisplayName();
            loserName = fighter1.getDisplayName();
        } else {
            winnerName = fighter1.getDisplayName();
            loserName = fighter2.getDisplayName();
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4" + winnerName + "&4 beat " + loserName + "&4 in the arena!"));
    }

    public boolean isFighter(Player player) {
        return (player == fighter1 || player == fighter2);
    }
    
}
