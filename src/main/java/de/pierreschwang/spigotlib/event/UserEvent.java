package de.pierreschwang.spigotlib.event;

import de.pierreschwang.spigotlib.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class UserEvent<T extends User> extends PlayerEvent {

    private final T user;

    public UserEvent(T user, Player who) {
        super(who);
        this.user = user;
    }

    public T getUser() {
        return user;
    }

}
