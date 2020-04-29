package de.pierreschwang.spigotlib.nms.v1_8_R3;

import de.pierreschwang.spigotlib.nms.CraftEntity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;

public class CraftEntityImpl implements CraftEntity {

    @Override
    public void setGravity(ArmorStand stand, boolean gravity) {
        EntityArmorStand armorStand = ((CraftArmorStand) stand).getHandle();
        armorStand.setGravity(gravity);
    }

}