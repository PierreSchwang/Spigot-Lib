package de.pierreschwang.spigotlib.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CraftEntity {

    private final Class<?> entityArmorStandClass = NmsHelper.getNmsClass("EntityArmorStand");
    private final Field entityIdField = NmsHelper.getField(NmsHelper.getNmsClass("Entity"), "id");
    private final Method worldServerMethod = NmsHelper.getMethod(NmsHelper.getCraftBukkitClass("CraftWorld"), "getHandle");
    private final Method gravityMethod = NmsHelper.getMethod(entityArmorStandClass, "setGravity", boolean.class);
    private final Method locationMethod = NmsHelper.getMethod(entityArmorStandClass, "setLocation", double.class, double.class, double.class, float.class, float.class);
    private final Method customNameVisibilityMethod = NmsHelper.getMethod(entityArmorStandClass, "setCustomNameVisible", boolean.class);
    private final Method customNameMethod = NmsHelper.getMethod(entityArmorStandClass, "setCustomName", String.class);
    private final Method invisibleMethod = NmsHelper.getMethod(entityArmorStandClass, "setInvisible", boolean.class);
    private final Class<?> packetPlayOutSpawnEntityLivingClass = NmsHelper.getNmsClass("PacketPlayOutSpawnEntityLiving");
    private final Class<?> packetPlayOutEntityDestroyClass = NmsHelper.getNmsClass("PacketPlayOutEntityDestroy");

    public NmsArmorStand spawnHologramLine(Player viewer, Location location, String display) {
        try {
            Object worldServer = worldServerMethod.invoke(location.getWorld());
            Object armorStand = entityArmorStandClass.getConstructor(NmsHelper.getNmsClass("World")).newInstance(worldServer);
            gravityMethod.invoke(armorStand, false);
            locationMethod.invoke(armorStand, location.getX(), location.getY(), location.getZ(), 0, 0);
            customNameVisibilityMethod.invoke(armorStand, true);
            customNameMethod.invoke(armorStand, display);
            invisibleMethod.invoke(armorStand, true);
            Object spawnEntityPacket = packetPlayOutSpawnEntityLivingClass.getConstructor(NmsHelper.getNmsClass("EntityLiving")).newInstance(armorStand);
            NmsHelper.sendPacket(viewer, spawnEntityPacket);
            int[] ids = new int[] {
                ((int) entityIdField.get(armorStand))
            };
            Object destroyEntityPacket = packetPlayOutEntityDestroyClass.getConstructor(int[].class).newInstance((Object) ids);
            return () -> NmsHelper.sendPacket(viewer, destroyEntityPacket);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
        return () -> {
        };
    }

}
