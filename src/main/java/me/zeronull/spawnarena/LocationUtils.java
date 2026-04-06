package me.zeronull.spawnarena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONObject;

public final class LocationUtils {
    public static JSONObject locationToJson(final Location loc) {
        final JSONObject obj = new JSONObject();

        obj.put("world", loc.getWorld().getName());
        obj.put("x", loc.getX());
        obj.put("y", loc.getY());
        obj.put("z", loc.getZ());
        obj.put("yaw", loc.getYaw());
        obj.put("pitch", loc.getPitch());

        return obj;
    }

    public static Location locationFromJson(final JSONObject obj) {
        final World world = Bukkit.getWorld(obj.getString("world"));
        final double x = obj.getDouble("x");
        final double y = obj.getDouble("y");
        final double z = obj.getDouble("z");
        final float yaw = obj.getFloat("yaw");
        final float pitch = obj.getFloat("pitch");

        final Location loc = new Location(world, x, y, z, yaw, pitch);
        return loc;
    }
}