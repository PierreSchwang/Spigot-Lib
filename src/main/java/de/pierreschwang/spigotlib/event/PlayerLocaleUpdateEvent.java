package de.pierreschwang.spigotlib.event;

import de.pierreschwang.spigotlib.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called whenever the locale for a player updates. This happens on join, and when the player changes the client
 * language.
 */
public class PlayerLocaleUpdateEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final String oldLocale;
    private final String newLocale;

    public PlayerLocaleUpdateEvent(Player who, String oldLocale, String newLocale) {
        super(who);
        this.oldLocale = oldLocale;
        this.newLocale = newLocale;
    }

    /**
     * Get the old language of the client.
     *
     * @return The old locale.
     */
    public String getOldLocale() {
        return oldLocale;
    }


    /**
     * Get the new language of the client.
     *
     * @return The new locale.
     */
    public String getNewLocale() {
        return newLocale;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}