package me.zeronull.spawnarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;

import me.zeronull.spawnarena.SpawnArena;
import net.md_5.bungee.api.ChatColor;

public class ArenaPreventCraftingSlot implements Listener {

    @EventHandler
    public void onClickInventory(final InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(!SpawnArena.arena.isFighter(player)) {
            return;
        }

        if (!(event.getSlotType() == InventoryType.SlotType.CRAFTING)) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't craft while in the arena!"));
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(!SpawnArena.arena.isFighter(player)) {
            return;
        }

        if (event.getInventory() instanceof CraftingInventory) {
            for (int slot : event.getRawSlots()) {
                if (slot > 0 && slot < 5) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't craft while in the arena!"));
                    return;
                }
            }
        }
    }
}
