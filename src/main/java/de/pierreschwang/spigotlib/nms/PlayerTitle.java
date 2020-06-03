package de.pierreschwang.spigotlib.nms;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PlayerTitle {

    private static Class<?> packetPlayOutTitleClass = NmsHelper.getNmsClass("PacketPlayOutTitle");;
    private static Class<?> enumTitleActionClass = NmsHelper.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
    private static Class<?> chatSerializerClass = NmsHelper.getNmsClass("IChatBaseComponent$ChatSerializer");

    public static void sendAll(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(player, title);
        sendSubtitle(player, subtitle);
        sendTimes(player, fadeIn, stay, fadeOut);
    }

    public static void sendTimes(Player player, int fadein, int stay, int fadeout) {
        Object packet = constructTitlePacket();
        try {
            NmsHelper.setField(packet, "a", NmsHelper.getField(enumTitleActionClass, "TIMES").get(null));
            NmsHelper.setField(packet, "c", fadein);
            NmsHelper.setField(packet, "d", stay);
            NmsHelper.setField(packet, "e", fadeout);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        NmsHelper.sendPacket(player, packet);
    }

    public static void sendTitle(Player player, String title) {
        Object packet = constructTitlePacket();
        try {
            NmsHelper.setField(packet, "a", NmsHelper.getField(enumTitleActionClass, "TITLE").get(null));
            NmsHelper.setField(packet, "b", NmsHelper.getMethod(chatSerializerClass, "a", String.class).invoke(null, "{'text': '" + title + "'}"));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        NmsHelper.sendPacket(player, packet);
    }

    public static void sendSubtitle(Player player, String subtitle) {
        Object packet = constructTitlePacket();
        try {
            NmsHelper.setField(packet, "a", NmsHelper.getField(enumTitleActionClass, "SUBTITLE").get(null));
            NmsHelper.setField(packet, "b", NmsHelper.getMethod(chatSerializerClass, "a", String.class).invoke(null, "{'text': '" + subtitle + "'}"));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        NmsHelper.sendPacket(player, packet);
    }

    public static void clear(Player player) {
        Object packet = constructTitlePacket();
        try {
            NmsHelper.setField(packet, "a", NmsHelper.getField(enumTitleActionClass, "CLEAR").get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        NmsHelper.sendPacket(player, packet);
    }

    private static Object constructTitlePacket() {
        try {
            return packetPlayOutTitleClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}