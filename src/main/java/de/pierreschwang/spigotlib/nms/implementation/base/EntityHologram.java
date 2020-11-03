package de.pierreschwang.spigotlib.nms.implementation.base;

import de.pierreschwang.spigotlib.nms.implementation.Implementation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface EntityHologram extends Implementation {

    int spawn(Player player, Location location, String text, boolean marker);

    void teleport(Player player, int entityId, Location newLocation);

}
