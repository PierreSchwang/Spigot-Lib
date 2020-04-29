package de.pierreschwang.spigotlib.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

public interface PlayerRenderer {

    /**
     * Defines the prefix for a player.
     *
     * @return A function which returns the prefix for a {@link Player}.
     */
    Function<Player, String> getPrefix();

    /**
     * Defines the suffix for a player.
     *
     * @return A function which returns the suffix for a {@link Player}.
     */
    Function<Player, String> getSuffix();

    /**
     * Defines the title of the sidebar scoreboard for a player.
     *
     * @return A function which returns the title for a {@link Player}.
     */
    Function<Player, String> getSidebarTitle();

    /**
     * Get the lines for the sidebar scoreboard for a specific player.
     *
     * @return The function for retrieving the lines.
     */
    Function<Player, String[]> getLines();

}
