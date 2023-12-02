//package me.zeronull.spawnarena.events;
//
//import me.zeronull.spawnarena.SpawnArena;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.Cancellable;
//import org.bukkit.event.Event;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.plugin.EventExecutor;
//
//import java.lang.reflect.Field;
//
//public final class ArenaCombatTagEvent implements Listener, EventExecutor {
//    public void registerEvent() {
//        try {
//            final Class<? extends Event> clazz = (Class<? extends Event>) Class.forName("net.badbird5907.anticombatlog.api.events.CombatTagEvent");
//            Bukkit.getPluginManager().registerEvent(clazz, this, EventPriority.MONITOR, this, SpawnArena.INSTANCE);
//        } catch (final Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    @Override
//    public void execute(final Listener listener, final Event e) {
//        try {
//            if (!(e instanceof Cancellable))
//                return;
//
//            final Cancellable cancellable = (Cancellable) e;
//
//            final Class<?> eventClass = e.getClass();
//            final Field attackerField = eventClass.getDeclaredField("attacker");
//
//            attackerField.setAccessible(true);
//
//            final Player attacker = (Player) attackerField.get(e);
//
//            if (SpawnArena.arenas.hasFighter(attacker))
//                cancellable.setCancelled(true);
//        } catch (final Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//}