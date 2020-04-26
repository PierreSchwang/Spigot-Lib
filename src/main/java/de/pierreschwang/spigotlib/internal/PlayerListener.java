package de.pierreschwang.spigotlib.internal;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final AbstractJavaPlugin<?> plugin;

    public PlayerListener(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getUserRepository().create(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getUserRepository().getUsers().remove(event.getPlayer());
    }

}
