package de.pierreschwang.spigotlib.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CraftEntity {

    NmsArmorStand spawnHologramLine(Player viewer, Location location, String display);

}
