package me.zeronull.spawnarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.zeronull.spawnarena.SpawnArena;
import net.md_5.bungee.api.ChatColor;

public class ArenaCommandEvent implements Listener {

    @EventHandler
    public void onCommandExecution(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if(!SpawnArena.arena.isFighter(player)) {
            return;
        }

        if(player.hasPermission("spawnarena.allowcommandsinfight")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't execute commands while in the arena!"));
    }
    
}
