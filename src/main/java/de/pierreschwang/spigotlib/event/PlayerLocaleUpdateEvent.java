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

    public PlayerLocaleUpdateEvent(Player who, String oldLocale) {
        super(who);
        this.oldLocale = oldLocale;
    }

    /**
     * Get the old language of the client. The new language can be accessed using {@link User#getLocale()}.
     *
     * @return The old locale.
     */
    public String getOldLocale() {
        return oldLocale;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}