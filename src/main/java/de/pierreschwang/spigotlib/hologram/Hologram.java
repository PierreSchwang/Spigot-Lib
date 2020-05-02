package de.pierreschwang.spigotlib.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private static final double MARGIN = .28;
    private final Location location;
    private final List<ArmorStand> lines;

    public Hologram(Location location, String... lines) {
        this.location = location.add(0, (MARGIN * lines.length), 0);
        this.lines = new ArrayList<>();
        addLines(lines);
    }

    public void destroy() {
        lines.forEach(Entity::remove);
    }

    public void setLine(int line, String content) {
        if (lines.get(line) == null)
            return;
        lines.get(line).setCustomName(content);
    }

    public void addLines(String... lines) {
        this.lines.forEach(stand -> {
            stand.teleport(location.add(0, (MARGIN * lines.length), 0));
        });
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            this.lines.add(spawnLine(location.clone().add(0, -(i * MARGIN), 0), line));
        }
    }

    private ArmorStand spawnLine(Location location, String line) {
        final ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName(line);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        return armorStand;
    }

}
