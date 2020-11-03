package de.pierreschwang.spigotlib.nms.implementation.implementations.v1_8_R3;

import de.pierreschwang.spigotlib.nms.implementation.base.EntityHologram;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class EntityHologram_v1_8_R3 implements EntityHologram {

    @Override
    public int spawn(Player player, Location location, String text, boolean marker) {
        World world = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(world);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.n(marker);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        return armorStand.getId();
    }

    @Override
    public void teleport(Player player, int entityId, Location newLocation) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(
                entityId,
                MathHelper.floor(newLocation.getX() * 32.0D),
                MathHelper.floor(newLocation.getY() * 32.0D),
                MathHelper.floor(newLocation.getZ() * 32.0D),
                (byte) ((int) (newLocation.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (newLocation.getPitch() * 256.0F / 360.0F)),
                false
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}