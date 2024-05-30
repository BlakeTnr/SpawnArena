package me.zeronull.spawnarena;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class WorldEditUtils {
    public static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    public static void paste(final Location loc, final File schematic) {
            SERVICE.execute(() -> {
                try {
                    ClipboardFormats.findByFile(schematic).load(schematic).paste(BukkitAdapter.adapt(loc.getWorld()), BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()), true, true, null);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            });
    }

    public static File getSchematic(final String name) {
        File schematic = new File("./plugins/WorldEdit/schematics/", name + ".schematic");

        if (!schematic.exists())
            schematic = new File("./plugins/WorldEdit/schematics/", name + ".schem");

        if (!schematic.exists())
            schematic = new File("./plugins/FastAsyncWorldEdit/schematics/" + name + ".schematic");

        if (!schematic.exists())
            schematic = new File("./plugins/FastAsyncWorldEdit/schematics/" + name + ".schem");

        return schematic;
    }
}