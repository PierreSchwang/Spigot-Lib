package de.pierreschwang.spigotlib.event;

import de.pierreschwang.spigotlib.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when the {@link User} instance is ready to use. That means, all internationalization methods can be used.
 */
public class PlayerReadyEvent<T extends User> extends UserEvent<T> {

    private static final HandlerList handlerList = new HandlerList();

    public PlayerReadyEvent(T user, Player who) {
        super(user, who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
