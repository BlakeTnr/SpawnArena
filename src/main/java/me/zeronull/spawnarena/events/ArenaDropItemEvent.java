package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.SpawnArena;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ArenaDropItemEvent implements Listener {
    @EventHandler
    public void onCommandExecution(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if(!SpawnArena.arenas.hasFighter(player)) {
            return;
        }

        if(player.hasPermission("spawnarena.allowdropinfight")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't drop items while in the arena!"));
    }
}
