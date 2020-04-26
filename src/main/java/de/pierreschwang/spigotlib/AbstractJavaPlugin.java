package de.pierreschwang.spigotlib;

import de.pierreschwang.spigotlib.command.CommandRegistry;
import de.pierreschwang.spigotlib.internal.PlayerListener;
import de.pierreschwang.spigotlib.lang.LanguageHandler;
import de.pierreschwang.spigotlib.user.User;
import de.pierreschwang.spigotlib.user.UserFactory;
import de.pierreschwang.spigotlib.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractJavaPlugin<T extends User> extends JavaPlugin {

    private LanguageHandler languageHandler;
    private CommandRegistry commandRegistry;
    private UserFactory<T> userFactory;
    private UserRepository<T> userRepository;

    @Override
    public void onEnable() {
        commandRegistry = new CommandRegistry(this);
        languageHandler = new LanguageHandler(this);
        userRepository = new UserRepository<>(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    public UserRepository<T> getUserRepository() {
        return userRepository;
    }

    public LanguageHandler getLanguageHandler() {
        return languageHandler;
    }

    public UserFactory<T> getUserFactory() {
        return userFactory;
    }

    public AbstractJavaPlugin<T> setUserFactory(UserFactory<T> userFactory) {
        this.userFactory = userFactory;
        return this;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public T getUser(Player player) {
        return userRepository.getUser(player);
    }

}
