package de.pierreschwang.spigotlib.user;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UserRepository<U extends User> {

    private final Map<Player, U> users = new HashMap<Player, U>();
    private final AbstractJavaPlugin<U> plugin;

    public UserRepository(AbstractJavaPlugin<U> plugin) {
        this.plugin = plugin;
    }

    public void create(Player player) {
        users.put(player, plugin.getUserFactory().constructUser(player));
    }

    public U getUser(Player player) {
        return users.get(player);
    }

    public Map<Player, U> getUsers() {
        return users;
    }

}
