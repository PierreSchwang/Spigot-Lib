package de.pierreschwang.spigotlib.user;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.entity.Player;

public class User {

    private final AbstractJavaPlugin<?> plugin;
    private final Player player;
    private final String locale;

    protected User(AbstractJavaPlugin<?> plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.locale = player.spigot().getLocale();
        plugin.getScoreboard().show(this).refresh();
    }

    public void sendMessage(String key, Object... params) {
        player.sendMessage(getMessage(key, params));
    }

    public String getMessage(String key, Object... params) {
        return plugin.getLanguageHandler().translate(locale, key, params);
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractJavaPlugin<?> getPlugin() {
        return plugin;
    }

    public String getLocale() {
        return locale;
    }
}