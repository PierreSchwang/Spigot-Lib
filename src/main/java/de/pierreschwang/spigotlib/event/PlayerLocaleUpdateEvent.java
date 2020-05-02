package de.pierreschwang.spigotlib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called whenever the locale for a player updates. This happens on join, and when the player changes the client
 * language.
 */
public class PlayerLocaleUpdateEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final String locale;

    public PlayerLocaleUpdateEvent(Player who, String locale) {
        super(who);
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}