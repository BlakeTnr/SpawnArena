package me.zeronull.spawnarena.events;

import me.zeronull.spawnarena.Arena;
import me.zeronull.spawnarena.Fight;
import me.zeronull.spawnarena.FightState;
import me.zeronull.spawnarena.SpawnArena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public final class ArenaPlayerInteractEvent implements Listener {
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!SpawnArena.arenas.hasActiveFight())
            return;

        if (!SpawnArena.arenas.hasFighter(player)) {
            return;
        }

        final Arena arena = SpawnArena.arenas.of(player);
        final Fight fight = arena.getFight(player);

        if (fight == null) {
            return;
        }

        if (fight.getState() == FightState.COUNTDOWN) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getState() instanceof InventoryHolder || e.getClickedBlock().getType() == Material.ENDER_CHEST)) {
            e.setCancelled(true);
            return;
        }

        if (!arena.isWinOnStepStonePressurePlate()) {
            return;
        }

        fight.announceWinner(fight.getOtherFighter(player));
        fight.endFight();
    }
}