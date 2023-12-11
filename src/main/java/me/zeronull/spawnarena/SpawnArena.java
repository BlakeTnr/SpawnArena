package me.zeronull.spawnarena;

import me.zeronull.spawnarena.commands.impl.CreateArenaCommand;
import me.zeronull.spawnarena.commands.impl.JoinArenaQueueCommand;
import me.zeronull.spawnarena.commands.impl.SetArenaSpawnCommand;
import me.zeronull.spawnarena.config.ConfigHandler;
import me.zeronull.spawnarena.events.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnArena extends JavaPlugin {
    public static SpawnArena INSTANCE;
    public static Arenas arenas;

    public SpawnArena() {
        INSTANCE = this;
    }

    public void onEnable() {
        registerEvents();
        registerCommands();

        arenas = Arenas.fromList(ConfigHandler.getInstance().registerArenasFromConfig());
    }

    private void registerEvents() {
        if (Arena.ArenaUtils.WORLD_EDIT_SUPPORT)
            Bukkit.getServer().getPluginManager().registerEvents(new ArenaWorldguardPvPEvent(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDeathEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaCommandEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaDropItemEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPreventCraftingSlot(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeave(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerLeaveAreaEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QueuePlayerLeave(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerLeaveSpawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerJoinEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LiquidListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerInteractEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaPlayerConsumeEvent(), this);
    }

    private void registerCommands() {
        this.registerCommand("joinarenaqueue", new JoinArenaQueueCommand());
        this.registerCommand("setarenaspawn", new SetArenaSpawnCommand());
        this.registerCommand("createarena", new CreateArenaCommand());
    }

    private void registerCommand(final String cmdName, final CommandExecutor cmd) {
        final PluginCommand command = super.getCommand(cmdName);

        command.setExecutor(cmd);

        if (cmd instanceof TabCompleter)
            command.setTabCompleter((TabCompleter) cmd);
    }

    public void onDisable() {
        this.shutdownArenas();
    }

    private void shutdownArenas() {
        if (arenas == null || arenas.isEmpty())
            return;

        for (final Arena arena : arenas) {
            arena.getFight().ifPresent(fight -> {
                if (fight.getState() == FightState.IN_FIGHT)
                    fight.endFight();
            });
        }

        Arena.ArenaUtils.kickOutLingeringPlayers();
    }
}
