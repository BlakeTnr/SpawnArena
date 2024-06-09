package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportKickOut implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        for(Arena arena : SpawnArena.arenas) {
            ArenaQueue queue = arena.queue;
            if(arena.queue.playerInQueue(player)) {
                arena.queue.removePlayer(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou were kicked out of the queue as you teleported!"));
            }

            for(Fight fight : arena.getFights()) {
                if(fight.getFighters().contains(player)) {
                    if(fight.getState() == FightState.COUNTDOWN) {
                        Player otherFighter = fight.getOtherFighter(player);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou were kicked out of the queue as you teleported!"));
                        otherFighter.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour opponent was kicked out of the queue!"));
                        fight.cancelFight();
                    }
                }
            }
        }
    }
}
