package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class ArenaPlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Arena.ArenaUtils.kickOutLingeringPlayer(e.getPlayer());
    }
}