package de.pierreschwang.spigotlib.hologram;

import de.pierreschwang.spigotlib.nms.NmsArmorStand;
import de.pierreschwang.spigotlib.nms.NmsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DynamicHologram {

    private static final double MARGIN = .28;
    private final Location location;
    private Function<Player, String[]> lines;
    private Map<Player, List<NmsArmorStand>> armorStands;

    public DynamicHologram(Location location, Function<Player, String[]> lines) {
        this.location = location;
        this.lines = lines;
        this.armorStands = new HashMap<>();
    }

    public void destroy() {
        armorStands.keySet().forEach(this::destroy);
    }

    public void destroy(Player player) {
        if(!armorStands.containsKey(player))
            return;
        armorStands.get(player).forEach(NmsArmorStand::remove);
        armorStands.remove(player);
    }

    public void spawn(Player player) {
        String[] lines = this.lines.apply(player);
        final Location location = this.location.clone();
        armorStands.put(player, new ArrayList<>());
        for (String line : lines) {
            armorStands.get(player).add(NmsHelper.getCraftEntity().spawnHologramLine(player, location.add(0, -MARGIN, 0), line));
        }
    }

    public void spawn() {
        Bukkit.getOnlinePlayers().forEach(this::spawn);
    }

    public void respawn(Player player) {
        destroy(player);
        spawn(player);
    }

    public void respawn() {
        destroy();
        spawn();
    }

}
