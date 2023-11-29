package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class ArenaPlayerLeave implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!SpawnArena.arenas.hasActiveFight()) {
            return;
        }

        if (!SpawnArena.arenas.hasFighter(player)) {
            return;
        }

        final Arena arena = SpawnArena.arenas.of(player);
        Optional<Fight> optionalFight = arena.getFight();
        if (optionalFight.isEmpty()) {
            return;
        }
        final Fight fight = optionalFight.get();

        if (fight.getState() == FightState.INITALIZING) {
            return;
        }

        fight.announceWinner(player);
        fight.endFight();
    }

}
