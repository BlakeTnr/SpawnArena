package me.zeronull.spawnarena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Fight {
    FightState fightState = FightState.INITALIZING;
    private Arena arena;
    PlayerPreFightData preFightData1;
    PlayerPreFightData preFightData2;
    Player fighter1;
    Player fighter2;

    public boolean bothFightersOnline() {
        return (fighter1.isOnline() && fighter2.isOnline());
    }

    public FightState getState() {
        return fightState;
    }

    public void cancelFight() {
        this.fightState = FightState.ENDING;

        fighter1 = null;
        fighter2 = null;

        this.fightState = FightState.OVER;

        this.arena.clearFight();

        this.arena.arenaState = ArenaState.EMPTY;
    }

    public boolean isFighter(Player player) {
        return this.getFighters().contains(player);
    }

    public void initiateFight(Player fighter1, Player fighter2, Arena arena) {
        this.arena = arena;
        arena.setFight(this);
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;

        Plugin plugin = SpawnArena.getPlugin(SpawnArena.class);
        new BukkitRunnable() {
            int counter = 5;

            @Override
            public void run() {
                final boolean onlineCheck = onlineCheck(this);

                if (onlineCheck)
                    return;

                if(counter == 0) {
                    startFight();
                    this.cancel();
                } else {
                    sendFightersMessage(ChatColor.translateAlternateColorCodes('&', "&6Starting in " + counter + "..."));
                    counter--;
                }
            }
            
        }.runTaskTimer(plugin, 20L, 20L);
    }

    private boolean onlineCheck(final BukkitRunnable runnable) {
        for (final Player fighter : this.getFighters()) {
            if (fighter.isOnline())
                continue;

            final String displayName = fighter.getDisplayName();
            this.getOtherFighter(fighter).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + displayName + " has left... cancelling fight"));
            cancelFight();
            arena.queue.tryStartFight();
            runnable.cancel();
            return true;
        }

        return false;
    }

    private void sendFightersMessage(final String message) {
        for (final Player fighter : this.getFighters()) {
            if (!this.isAvailable(fighter))
                continue;

            fighter.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    private void performOnFighters(final Consumer<Player> action) {
        for (final Player fighter : this.getFighters()) {
            if (!this.isAvailable(fighter))
                continue;

            action.accept(fighter);
        }
    }

    private boolean isAvailable(final Player p) {
        return p != null && p.isOnline();
    }

    private List<Player> getFighters() {
        return Arrays.asList(this.fighter1, this.fighter2);
    }

    private Player getOtherFighter(final Player fighter) {
        for (final Player p : this.getFighters()) {
            if (p.equals(fighter))
                continue;

            return p;
        }

        return null; // The other fighter could not be found
    }

    public void startFight() {
        Arena.ArenaUtils.kickOutLingeringPlayers();

        this.performOnFighters(fighter -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "invsave " + fighter.getName()));

        preFightData1 = new PlayerPreFightData(fighter1);
        preFightData2 = new PlayerPreFightData(fighter2);

        if (this.arena.isShouldClearItems())
            this.performOnFighters(fighter -> fighter.getInventory().clear());
        
        this.arena.teleportFighters(fighter1, fighter2);
        this.fightState = FightState.IN_FIGHT;
    }

    public void endFight() {
        this.fightState = FightState.ENDING;
        this.preFightData1.restore();
        this.preFightData2.restore();
        this.fightState = FightState.OVER;
        this.arena.clearFight();
//        this.arena.arenaState = ArenaState.EMPTY;

        this.arena.queue.tryStartFight();
        this.arena.arenaState = ArenaState.EMPTY;
    }

    public void announceWinner(Player whoDied) {
        String winnerName;
        String loserName;
        if(fighter1.equals(whoDied)) {
            winnerName = fighter2.getDisplayName();
            loserName = fighter1.getDisplayName();
        } else {
            winnerName = fighter1.getDisplayName();
            loserName = fighter2.getDisplayName();
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4" + winnerName + "&4 beat " + loserName + "&4 in the arena!"));
    }
}
