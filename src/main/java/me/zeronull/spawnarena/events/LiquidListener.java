package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import me.zeronull.spawnarena.events.impl.PlayerLiquidEnterEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class LiquidListener implements Listener, Runnable {
    public LiquidListener() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SpawnArena.INSTANCE, this, 0L, (3 * 20L));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(final PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getBlock().isLiquid())
            SpawnArena.INSTANCE.getServer().getPluginManager().callEvent(new PlayerLiquidEnterEvent(e.getPlayer()));
    }

    @Override
    public void run() {
        for (final Player p : SpawnArena.INSTANCE.getServer().getOnlinePlayers()) {
            if (p.getLocation().getBlock().isLiquid())
                SpawnArena.INSTANCE.getServer().getPluginManager().callEvent(new PlayerLiquidEnterEvent(p));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEnterLiquid(final PlayerLiquidEnterEvent e) {
        final Player player = e.getPlayer();

        if (!SpawnArena.arenas.hasActiveFight()) {
            return;
        }

        if (!SpawnArena.arenas.hasFighter(player)) {
            return;
        }

        final Arena arena = SpawnArena.arenas.of(player);
        final Fight fight = arena.getFight(player);

        if (fight == null) {
            return;
        }

        if (fight.getState() == FightState.INITALIZING) {
            return;
        }

        if (!arena.isDeathOnTouchLiquid()) {
            return;
        }

        fight.announceWinner(player);
        fight.endFight();
    }
}