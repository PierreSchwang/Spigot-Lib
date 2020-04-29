package de.pierreschwang.spigotlib.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

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

    public static Object constructChatMessage(String content) {
        try {
            Object chatMessage = getNmsClass("ChatMessage").getConstructor(String.class, Object[].class).newInstance(content, new Object[0]);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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

    public static String getPackage() {
        String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return "net.minecraft.server." + ver;
    }

    public static CraftEntity getCraftEntity() {
        return craftEntity;
    }
}
