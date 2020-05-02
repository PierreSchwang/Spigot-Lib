package de.pierreschwang.spigotlib.nms.v1_8_R3;

import de.pierreschwang.spigotlib.nms.CraftEntity;
import de.pierreschwang.spigotlib.nms.NmsArmorStand;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CraftEntityImpl implements CraftEntity {

    @Override
    public NmsArmorStand spawnHologramLine(Player viewer, Location location, String display) {
        // Construct hologram
        final WorldServer server = ((CraftWorld) location.getWorld()).getHandle();
        final EntityArmorStand armorStand = new EntityArmorStand(server);
        armorStand.setGravity(false);
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(display);
        armorStand.setInvisible(true);
        // send hologram
        PacketPlayOutSpawnEntityLiving spawnEntity = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(spawnEntity);
        return () -> {
            PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(armorStand.getId());
            ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(entityDestroy);
        };
    }

}