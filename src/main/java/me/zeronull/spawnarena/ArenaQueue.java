package me.zeronull.spawnarena;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ArenaQueue {
    Arena arena;
    private static ArenaQueue singletonInstance = null;
    ArrayList<Player> playerQueue = new ArrayList<Player>();

    private ArenaQueue() {
    }

    public static ArenaQueue getInstance() {
        if(!(singletonInstance instanceof ArenaQueue)) {
            singletonInstance = new ArenaQueue();
        }
        return singletonInstance;
    }

    public void removePlayer(Player player) {
        playerQueue.remove(player);
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    private boolean queueHasAtLeast2Players() {
        return (playerQueue.size() >= 2);
    }

    public boolean playerInQueue(Player player) {
        return playerQueue.contains(player);
    }

    public void tryStartFight() {
        boolean noCurrentFight = arena.arenaState == ArenaState.EMPTY;
        if(queueHasAtLeast2Players() && noCurrentFight) {
            Player[] twoOldest = pop2OldestPlayers();
            new Fight().initiateFight(twoOldest[0], twoOldest[1], arena);
        }
    }
    
    public void addPlayerToQueue(Player player) {
        playerQueue.add(player);
        tryStartFight();
    }

    public Player[] pop2OldestPlayers() {
        int size = playerQueue.size();
        Player player1 = playerQueue.get(size-1);
        playerQueue.remove(size-1);
        Player player2 = playerQueue.get(size-2);
        playerQueue.remove(size-2);

        Player[] twoOldest = {player1, player2};
        return twoOldest;
    }

    public Player getNextPlayerInQueue() {
        return playerQueue.get(0);
    }

    public Player popNextPlayerInQueue() {
        Player nextPlayer = playerQueue.get(0);
        playerQueue.remove(0);
        return nextPlayer;
    }
}
