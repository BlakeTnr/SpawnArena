package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.SpawnArena;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ArenaCommandEvent implements Listener {

    @EventHandler
    public void onCommandExecution(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if(!(SpawnArena.arena.getFight() instanceof Fight)) {
            return;
        }

        if(!SpawnArena.arena.getFight().isFighter(player)) {
            return;
        }

        if(player.hasPermission("spawnarena.allowcommandsinfight")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't execute commands while in the arena!"));
    }
    
}
