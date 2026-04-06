package me.zeronull.spawnarena.inventory;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.Base64;

public final class ItemSerializer {
    private static Method serializeMethod;
    private static Method deserializeMethod;
    private static boolean paperAvailable;

    static {
        try {
            final Class<?> clazz = ItemStack.class;

            serializeMethod = clazz.getMethod("serializeItemsAsBytes", ItemStack[].class);
            deserializeMethod = clazz.getMethod("deserializeItemsFromBytes", byte[].class);

            paperAvailable = true;
        } catch (final Exception ignored) {
            paperAvailable = false;
        }
    }

    public static String serialize(final ItemStack[] items) throws Exception {
        if (paperAvailable) {
            try {
                byte[] data = (byte[]) serializeMethod.invoke(null, (Object) items);
                return Base64.getEncoder().encodeToString(data);
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }

        return fallbackSerialize(items);
    }

    public static ItemStack[] deserialize(final String data) throws Exception {
        final byte[] decoded = Base64.getDecoder().decode(data);

        if (paperAvailable) {
            try {
                return (ItemStack[]) deserializeMethod.invoke(null, decoded);
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }

        return fallbackDeserialize(new String(decoded));
    }

    private static String fallbackSerialize(ItemStack[] items) throws Exception {
        try {
            final ReadWriteNBT nbt = NBT.itemStackArrayToNBT(items);
            return Base64.getEncoder().encodeToString(nbt.toString().getBytes());
        } catch (Exception e) {
            throw new Exception("NBT fallback serialize failed", e);
        }
    }

    private static ItemStack[] fallbackDeserialize(String json) throws Exception {
        try {
            final ReadWriteNBT nbt = NBT.parseNBT(json);
            return NBT.itemStackArrayFromNBT(nbt);
        } catch (Exception e) {
            throw new Exception("NBT fallback deserialize failed", e);
        }
    }
}