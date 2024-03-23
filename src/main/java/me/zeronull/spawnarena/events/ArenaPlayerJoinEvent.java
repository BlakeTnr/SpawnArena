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
//
//    private void handleToShow(final UUID uuid) {
//        if (!Fight.TO_SHOW.containsKey(uuid) && !Fight.TO_SHOW.containsValue(uuid))
//            return;
//
//        for (final Map.Entry<UUID, UUID> entry : Fight.TO_SHOW.entrySet()) {
//            final UUID uuid1 = entry.getKey();
//            final UUID uuid2 = entry.getValue();
//
//            final Player p1 = Bukkit.getPlayer(uuid1);
//            final Player p2 = Bukkit.getPlayer(uuid2);
//
//            if (p1 == null || p2 == null)
//                continue;
//
//            if (!p1.isOnline() || !p2.isOnline())
//                continue;
//
//            p1.showPlayer(p2);
//            p2.showPlayer(p1);
//        }
//    }
}