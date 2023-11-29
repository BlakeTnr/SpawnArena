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
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnArena extends JavaPlugin {
    public static Arenas arenas;

    private void registerEvents() {
        if (Arena.ArenaUtils.WORLD_EDIT_SUPPORT)
            Bukkit.getServer().getPluginManager().registerEvents(new ArenaWorldguardPvPEvent(), this);

        this.registerListeners(
                new ArenaDeathEvent(),
                new ArenaCommandEvent(),
                new ArenaDropItemEvent(),
                new ArenaPreventCraftingSlot(),
                new ArenaPlayerLeave(),
                new ArenaPlayerLeaveAreaEvent(),
                new QueuePlayerLeave(),
                new PlayerLeaveSpawn(),
                new ArenaPlayerJoinEvent()
        );
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommands() {
        this.registerCommand("joinarenaqueue", new JoinArenaQueueCommand());
        this.registerCommand("setarenaspawn", new SetArenaSpawnCommand());
        this.registerCommand("createarena", new CreateArenaCommand());
    }

    private void registerCommand(final String cmdName, final CommandExecutor cmd) {
        final PluginCommand command = super.getCommand(cmdName);

        if (command != null) {
            command.setExecutor(cmd);

            if (cmd instanceof TabCompleter)
                command.setTabCompleter((TabCompleter) cmd);
        }
    }

    public void onEnable() {
        registerEvents();
        registerCommands();

        arenas = Arenas.fromList(ConfigHandler.getInstance().registerArenasFromConfig());
    }

    public void onDisable() {
        this.shutdownArenas();
    }

    private void shutdownArenas() {
//        System.out.println(String.format("Null or empty: %s", arenas == null || arenas.isEmpty()));

        if (arenas == null || arenas.isEmpty())
            return;

        for (final Arena arena : arenas) {
//            System.out.println(String.format("null: %s", arena == null));
            arena.getFight().ifPresent(fight -> {
                if (fight.getState() == FightState.IN_FIGHT)
                    fight.endFight();
            });
        }

        Arena.ArenaUtils.kickOutLingeringPlayers();
    }
}
