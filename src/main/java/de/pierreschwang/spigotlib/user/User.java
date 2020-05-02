package de.pierreschwang.spigotlib.user;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import de.pierreschwang.spigotlib.nms.NmsHelper;
import de.pierreschwang.spigotlib.util.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class User {

    private final AbstractJavaPlugin<?> plugin;
    private final Player player;
    private String locale;
    private Map<Consumer<String>, Pair<String, Object[]>> toTranslate;

    protected User(AbstractJavaPlugin<?> plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        toTranslate = new HashMap<>();
        this.locale = null;
        plugin.getScoreboard().show(this).refresh();
    }

    public void sendMessage(String key, Object... params) {
        getMessage(key, player::sendMessage, params);
    }

    public void getMessage(String key, Consumer<String> translated, Object... params) {
        if(locale == null) {
            toTranslate.put(translated, new Pair<>(key, params));
            return;
        }
        translated.accept(plugin.getLanguageHandler().translate(locale, key, params));
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractJavaPlugin<?> getPlugin() {
        return plugin;
    }

    public void setLocale(String locale) {
        this.locale = locale;
        if(toTranslate.isEmpty()) {
            return;
        }
        toTranslate.forEach((translated, stringPair) -> translated.accept(plugin.getLanguageHandler().translate(locale, stringPair.getLeft(), stringPair.getRight())));
        toTranslate.clear();
    }

    public String getLocale() {
        return locale;
    }
}