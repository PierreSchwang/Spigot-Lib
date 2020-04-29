package de.pierreschwang.spigotlib.hologram;

import de.pierreschwang.spigotlib.nms.NmsHelper;
import de.pierreschwang.spigotlib.util.Pair;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;

public class Hologram {

    private static final double MARGIN = .28;
    private final Location location;
    private final Map<Integer, Pair<ArmorStand, String>> lines;

    public Hologram(Location location, String... lines) {
        this.location = location.add(0, (MARGIN * lines.length), 0);
        this.lines = new HashMap<>();
        addLines(lines);
    }

    public void destroy() {
        lines.values().forEach(pair -> pair.getLeft().remove());
    }

    public void setLine(int line, String content) {
        if (!lines.containsKey(line))
            return;
        Pair<ArmorStand, String> pair = lines.get(line);
        pair.setRight(content);
        pair.getLeft().setCustomName(content);
    }

    public void addLines(String... lines) {
        this.lines.values().forEach(pair -> {
            pair.getLeft().teleport(pair.getLeft().getLocation().add(0, (MARGIN * lines.length), 0));
        });
        for (String line : lines) {
            this.lines.put(this.lines.size(), new Pair<>(
                spawnLine(location.clone().add(0, -((this.lines.size() - 1) * MARGIN), 0), line), line));
        }
    }

    private ArmorStand spawnLine(Location location, String line) {
        final ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName(line);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        NmsHelper.getCraftEntity().setGravity(armorStand, false);
        return armorStand;
    }

}
