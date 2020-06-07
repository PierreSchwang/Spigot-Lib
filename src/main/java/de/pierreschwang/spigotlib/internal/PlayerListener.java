package de.pierreschwang.spigotlib.internal;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import de.pierreschwang.spigotlib.event.PlayerReadyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final AbstractJavaPlugin<?> plugin;
    private final PlayerPacketInterceptor playerPacketInterceptor;

    public PlayerListener(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        this.playerPacketInterceptor = new PlayerPacketInterceptor(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        plugin.getUserRepository().create(event.getPlayer());
        playerPacketInterceptor.register(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerReady(PlayerReadyEvent<?> event) {
        plugin.getScoreboard().show(event.getUser()).refresh();
        plugin.getScoreboard().refresh(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerPacketInterceptor.unregister(event.getPlayer());
        plugin.getScoreboard().hide(plugin.getUserRepository().getUser(event.getPlayer()));
        plugin.getUserRepository().getUsers().remove(event.getPlayer());
        plugin.getScoreboard().refresh(event.getPlayer());
    }

}
