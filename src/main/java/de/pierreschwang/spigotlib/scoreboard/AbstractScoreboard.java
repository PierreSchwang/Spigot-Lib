package de.pierreschwang.spigotlib.scoreboard;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import de.pierreschwang.spigotlib.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AbstractScoreboard {

    private final Map<User, InternalScoreboard<?>> scoreboards = new HashMap<>();
    private final AbstractJavaPlugin<?> plugin;

    public AbstractScoreboard(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
    }

    public <T extends User> InternalScoreboard<?> show(T user) {
        if(scoreboards.containsKey(user))
            return scoreboards.get(user);
        InternalScoreboard<?> scoreboard = new InternalScoreboard<>(this, user);
        scoreboards.put(user, scoreboard);
        return scoreboard;
    }

    public <T extends User> void hide(T user) {
        scoreboards.remove(user);
        user.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void refresh() {
        scoreboards.values().forEach(InternalScoreboard::refresh);
    }

    public void refresh(Player... players) {
        scoreboards.values().forEach(internalScoreboard -> internalScoreboard.refresh(players));
    }

    public AbstractJavaPlugin<?> getPlugin() {
        return plugin;
    }
}
