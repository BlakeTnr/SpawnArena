package me.zeronull.spawnarena;

import club.hellin.vivillyapi.SpigotCoreBase;
import club.hellin.vivillyapi.models.impl.PlayerStateBase;
import club.hellin.vivillyapi.models.impl.objects.ArenaStatsBase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class Fight {
    private static final ChatColor[] VIBRANT_COLORS = {
            ChatColor.RED,
            ChatColor.BLUE,
            ChatColor.GREEN,
            ChatColor.YELLOW,
            ChatColor.AQUA,
            ChatColor.WHITE,
            ChatColor.LIGHT_PURPLE,
            ChatColor.GRAY,
            ChatColor.GOLD,
            ChatColor.BLUE
    };

    UUID uuid = UUID.randomUUID();
    FightState fightState = FightState.INITALIZING;
    Arena arena;
    PlayerPreFightData preFightData1;
    PlayerPreFightData preFightData2;
    Player fighter1;
    Player fighter2;
    BukkitTask starting;
    ChatColor color;

    private Map<UUID, ChatColor> teams = new HashMap<>(); // UUID -> Original ChatColor

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

        this.arena.removeFight(this);
        this.tryCancel(this.starting);
        this.performShowLogic();

//        this.arena.arenaState = ArenaState.EMPTY;
    }

    public boolean isFighter(Player player) {
        return this.getFighters().contains(player);
    }

    public void initiateFight(Player fighter1, Player fighter2, Arena arena) {
        this.arena = arena;
        arena.addFight(this);
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;

        Plugin plugin = SpawnArena.getPlugin(SpawnArena.class);
        this.starting = new BukkitRunnable() {
            int counter = 5;

            @Override
            public void run() {
                final boolean onlineCheck = onlineCheck(this);

                if (onlineCheck)
                    return;

                if (counter == 0) {
                    startFight();
                    this.cancel();
                } else {
                    sendFightersMessage(ChatColor.translateAlternateColorCodes('&', "&6Starting in " + counter + "..."));
                    counter--;
                }
            }

        }.runTaskTimer(plugin, 20L, 20L);
    }

    private void tryCancel(final BukkitTask task) {
        if (task != null && !task.isCancelled())
            task.cancel();
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

    public List<Player> getFighters() {
        return Arrays.asList(this.fighter1, this.fighter2);
    }

    public Player getOtherFighter(final Player fighter) {
        for (final Player p : this.getFighters()) {
            if (p.equals(fighter))
                continue;

            return p;
        }

        return null; // The other fighter could not be found
    }

    public void startFight() {
        Arena.ArenaUtils.kickOutLingeringPlayers();

        this.color = this.getRandomColor();

        this.performOnFighters(fighter -> fighter.setInvulnerable(false));
        this.performOnFighters(this::leaveParkour);
        this.performOnFighters(HumanEntity::closeInventory);
        this.performOnFighters(fighter -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "invsave " + fighter.getName()));

        preFightData1 = new PlayerPreFightData(fighter1);
        preFightData2 = new PlayerPreFightData(fighter2);

        this.performOnFighters(fighter -> fighter.setGameMode(GameMode.SURVIVAL));
        this.performOnFighters(fighter -> fighter.setHealth(fighter.getMaxHealth()));
        this.performOnFighters(fighter -> fighter.setFoodLevel(20));

        if (this.arena.isShouldClearItems()) {
            this.performOnFighters(fighter -> fighter.getInventory().clear());
            this.performOnFighters(fighter -> {
                for (final PotionEffect effect : new ArrayList<>(fighter.getActivePotionEffects()))
                    fighter.removePotionEffect(effect.getType());
            });
        }

        if (this.arena.isGiveKnockBackStick()) {
            final ItemStack stick = new ItemStack(Material.STICK);
            stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);

            this.performOnFighters(fighter -> fighter.getInventory().addItem(stick));
        }

        this.arena.teleportFighters(fighter1, fighter2);

        this.performHideLogic();
        this.fightState = FightState.IN_FIGHT;
    }

    private ChatColor getRandomColor() {
        final Random random = new Random();
        final int index = random.nextInt(VIBRANT_COLORS.length);
        return VIBRANT_COLORS[index];
    }

    public void clearTeams() {
        this.performOnFighters(fighter -> fighter.setGlowing(false));
    }

    private void leaveParkour(final Player player) {
        try {
            final Class<?> clazz = Class.forName("io.github.a5h73y.parkour.Parkour");
            final Method instanceMethod = clazz.getDeclaredMethod("getInstance");
            final Object main = instanceMethod.invoke(null);

            final Method playerManagerMethod = clazz.getDeclaredMethod("getPlayerManager");
            final Object playerManager = playerManagerMethod.invoke(main);

            final Method parkourSessionManagerMethod = clazz.getDeclaredMethod("getParkourSessionManager");
            final Object parkourSessionManager = parkourSessionManagerMethod.invoke(main);

            final Class<?> parkourSessionManagerClass = parkourSessionManager.getClass();
            final Method isPlayingMethod = parkourSessionManagerClass.getDeclaredMethod("isPlayingParkourCourse", Player.class);
            final boolean isPlaying = (boolean) isPlayingMethod.invoke(parkourSessionManager, player);

            if (isPlaying) {
                final Class<?> playerManagerClass = playerManager.getClass();
                final Method leaveCourseMethod = playerManagerClass.getDeclaredMethod("leaveCourse", Player.class);
                leaveCourseMethod.invoke(playerManager, player);
            }
        } catch (Exception ignored) {
            System.out.println("Parkour not loaded, skipping...");
        }
    }

    private void performHideLogic() {
        for (final Player fighter : this.getFighters()) {
            for (final Player other : this.arena.getOtherPlayers(this)) {
                if (fighter == null || other == null)
                    continue;

                if (!fighter.isOnline() || !other.isOnline())
                    continue;

                fighter.hidePlayer(SpawnArena.INSTANCE, other);
                other.hidePlayer(SpawnArena.INSTANCE, fighter);
            }
        }
    }

    private void performShowLogic() {
        for (final Player fighter : this.getFighters()) {
            for (final Player other : this.arena.getOtherPlayers(this)) {
                if (fighter == null || other == null)
                    continue;

                if (!fighter.isOnline() || !other.isOnline())
                    continue;

                fighter.showPlayer(SpawnArena.INSTANCE, other);
                other.showPlayer(SpawnArena.INSTANCE, fighter);
            }
        }
    }

    public void teleportToSpawn(final Player fighter) {
        final UUID uuid = fighter.getUniqueId();

        if (this.fighter1 != null && this.fighter1.getUniqueId().equals(uuid))
            fighter.teleport(this.arena.spawnPoint1);
        else if (this.fighter2 != null && this.fighter2.getUniqueId().equals(uuid))
            fighter.teleport(this.arena.spawnPoint2);
    }

    public void endFight() {
        this.fightState = FightState.ENDING;

        this.preFightData1.restore();
        this.preFightData2.restore();

        this.fightState = FightState.OVER;
        this.arena.removeFight(this);
//        this.arena.arenaState = ArenaState.EMPTY;

//        this.arena.arenaState = ArenaState.EMPTY;
        this.arena.queue.tryStartFight();
        this.tryCancel(this.starting);
        this.performShowLogic();
        this.clearTeams();

        if (this.getArena() instanceof SavableArena && this.getArena().getArenaMode() == ArenaMode.SINGLE) {
            final SavableArena savableArena = (SavableArena) this.getArena();
            savableArena.resetArena();
        }
    }

    public void announceWinner(Player whoDied) {
        Player winner;
        Component winnerName;

        Player loser;
        Component loserName;

        if (fighter1.equals(whoDied)) {
            winner = fighter2;
            winnerName = fighter2.displayName();

            loser = fighter1;
            loserName = fighter1.displayName();
        } else {
            winner = fighter1;
            winnerName = fighter1.displayName();

            loser = fighter2;
            loserName = fighter2.displayName();
        }

        this.handleDeath(loser);
        this.handleVictory(winner);

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<dark_red><winner> beat <loser> in the <arena>arena!",
                Placeholder.component("winner", winnerName),
                Placeholder.component("loser", loserName),
                Placeholder.parsed("arena", "arena".equals(this.arena.getArenaName()) ? "" : this.arena.getArenaName() + " ")
        ));
    }

    private void handleDeath(final Player loser) {
        ArenaStatsBase.Values winStreakValue = this.getValue(this.arena.arenaName, ValueType.STREAK);
        PlayerStateBase state = SpigotCoreBase.INSTANCE.getWs().getPlayerStateMap().get(loser.getUniqueId());

        if (state != null && winStreakValue != null) {
            final ArenaStatsBase stats = state.getArenaStats();

            stats.setInt(winStreakValue, 0);
            SpigotCoreBase.INSTANCE.getWs().updatePlayerState(state);
        }
    }

    public Arena getArena() {
        return this.arena;
    }

    private void handleVictory(final Player winner) {
        ArenaStatsBase.Values winStreakValue = this.getValue(this.arena.arenaName, ValueType.STREAK);
        ArenaStatsBase.Values winsValue = this.getValue(this.arena.arenaName, ValueType.WINS);
        ArenaStatsBase.Values bestWinStreakValue = this.getValue(this.arena.arenaName, ValueType.BEST_STREAK);

        PlayerStateBase state = SpigotCoreBase.INSTANCE.getWs().getPlayerStateMap().get(winner.getUniqueId());

        if (state != null && winStreakValue != null && winsValue != null && bestWinStreakValue != null) {
            final ArenaStatsBase stats = state.getArenaStats();

            final int newWinStreak = stats.getInt(winStreakValue) + 1;

            stats.setInt(winStreakValue, newWinStreak);
            stats.setInt(winsValue, stats.getInt(winsValue) + 1);

            if (newWinStreak > stats.getInt(bestWinStreakValue))
                stats.setInt(bestWinStreakValue, newWinStreak);

            SpigotCoreBase.INSTANCE.getWs().updatePlayerState(state);
        }
    }

    private ArenaStatsBase.Values getValue(final String arenaName, final ValueType type) {
        return switch (type) {
            case WINS -> switch (arenaName.toLowerCase()) {
                case "arena" -> ArenaStatsBase.Values.ARENA_WINS;
                case "sumo" -> ArenaStatsBase.Values.SUMO_WINS;
                default -> null;
            };
            case STREAK -> switch (arenaName.toLowerCase()) {
                case "arena" -> ArenaStatsBase.Values.ARENA_WIN_STREAK;
                case "sumo" -> ArenaStatsBase.Values.SUMO_WIN_STREAK;
                default -> null;
            };
            case BEST_STREAK -> switch (arenaName.toLowerCase()) {
                case "arena" -> ArenaStatsBase.Values.BEST_ARENA_WIN_STREAK;
                case "sumo" -> ArenaStatsBase.Values.BEST_SUMO_WIN_STREAK;
                default -> null;
            };
            default -> null;
        };

    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Fight fight))
            return false;

        return fight.uuid.equals(this.uuid);
    }

    public enum ValueType {
        WINS,
        STREAK,
        BEST_STREAK;
    }
}
