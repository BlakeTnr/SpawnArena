package me.zeronull.spawnarena;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Fight {
    // Todo: pull out some of the code from Arena into here
    FightState fightState = FightState.INITALIZING;
    private Arena arena;
    // Todo: Create PlayerPreFightData class
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
        return (player.equals(fighter1) || player.equals(fighter2));
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
                if(!fighter1.isOnline()) {
                    String displayName = fighter1.getDisplayName();
                    fighter2.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + displayName + " has left... cancelling fight"));
                    cancelFight();
                    arena.queue.tryStartFight();
                    this.cancel();
                    return;
                }

                if(!fighter2.isOnline()) {
                    String displayName = fighter2.getDisplayName();
                    fighter1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + displayName + " has left... cancelling fight"));
                    cancelFight();
                    arena.queue.tryStartFight();
                    this.cancel();
                    return;
                }

                if(counter == 0) {
                    startFight();
                    this.cancel();
                } else {
                    fighter1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Starting in " + counter + "..."));
                    fighter2.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Starting in " + counter + "..."));
                    counter--;
                }
            }
            
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void startFight() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "invsave " + fighter1.getName());
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "invsave " + fighter2.getName());
        preFightData1 = new PlayerPreFightData(fighter1);
        preFightData2 = new PlayerPreFightData(fighter2);
        
        this.arena.teleportFighters(fighter1, fighter2);
        this.fightState = FightState.IN_FIGHT;
    }

    public void endFight() {
        this.fightState = FightState.ENDING;
        this.preFightData1.restore();
        this.preFightData2.restore();
        this.fightState = FightState.OVER;
        this.arena.clearFight();
        this.arena.arenaState = ArenaState.EMPTY;

        this.arena.queue.tryStartFight();
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
