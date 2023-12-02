package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ArenaPlayerInteractEvent implements Listener {
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if(!SpawnArena.arenas.hasFighter(player)) {
            return;
        }

        final Arena arena = SpawnArena.arenas.of(player);
        final Fight fight = arena.getFight().orElse(null);

        if (fight == null) {
            return;
        }

        if(fight.getState() == FightState.INITALIZING) {
            return;
        }

        if (!arena.isWinOnStepStonePressurePlate()) {
            return;
        }

        fight.announceWinner(fight.getOtherFighter(player));
        fight.endFight();
    }
}