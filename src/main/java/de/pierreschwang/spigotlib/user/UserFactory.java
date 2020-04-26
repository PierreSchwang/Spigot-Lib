package de.pierreschwang.spigotlib.user;

import org.bukkit.entity.Player;

public interface UserFactory<T extends User> {

    T constructUser(Player player);

}
