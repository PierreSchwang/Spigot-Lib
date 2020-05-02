package de.pierreschwang.spigotlib.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NmsHelper {

    private static CraftEntity craftEntity;

    static {
        craftEntity = loadImplementation(CraftEntity.class);
    }

    private static <T> T loadImplementation(Class<T> base) {
        String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String path = "de.pierreschwang.spigotlib.nms." + ver + ".";
        try {
            Class<T> clazz = (Class<T>) Class.forName(path + base.getSimpleName() + "Impl");
            System.out.println("Using implementation " + path + clazz.getSimpleName());
            return clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Couldn't find a implementation for " + base.getSimpleName() + " with version " + ver);
            e.printStackTrace();
        }
        return null;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object playerHandle = playerHandle(player);
            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Object playerHandle(Player player) {
        try {
            return player.getClass().getMethod("getHandle").invoke(player);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNmsClass(String name) {
        try {
            return Class.forName(getPackage() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setField(Object object, String name, Object value) {
        try {
            getField(object, name).set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getField(Object object, String name) {
        return getField(object.getClass(), name);
    }

    public static <T> T getFieldValue(Class<?> clazz, String fieldName, Object instance) {
        try {
            return (T) getField(clazz, fieldName).get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPlayerLocale(Player player) {
        Object playerHandle = playerHandle(player);
        return getFieldValue(playerHandle.getClass(), "locale", playerHandle);
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... types) {
        try {
            return clazz.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackage() {
        String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return "net.minecraft.server." + ver;
    }

    public static CraftEntity getCraftEntity() {
        return craftEntity;
    }
}
