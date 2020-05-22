package de.pierreschwang.spigotlib.scoreboard;

import de.pierreschwang.spigotlib.user.User;

import java.util.function.Function;

public interface PlayerRenderer {

    /**
     * Defines the prefix for a player.
     *
     * @return A function which returns the prefix for a {@link User}.
     */
    Function<User, String> getPrefix();

    /**
     * Defines the suffix for a player.
     *
     * @return A function which returns the suffix for a {@link User}.
     */
    Function<User, String> getSuffix();

    /**
     * Defines the title of the sidebar scoreboard for a player.
     *
     * @return A function which returns the title for a {@link User}.
     */
    Function<User, String> getSidebarTitle();

    /**
     * Get the lines for the sidebar scoreboard for a specific player.
     *
     * @return The function for retrieving the lines.
     */
    Function<User, String[]> getLines();

}
