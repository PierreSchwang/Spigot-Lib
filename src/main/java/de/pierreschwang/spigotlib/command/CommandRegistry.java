package de.pierreschwang.spigotlib.command;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Registers commands directly to the command map, which does not require to put commands in the plugin.yml Also,
 * commands may be registered while the server is running.
 */
public class CommandRegistry {

    private static final String NO_PERMISSION_KEY = "command-no-permissions";
    private static final String NO_PERMISSION_FALLBACK = "§cYou dont have permission to do that!";

    private final AbstractJavaPlugin<?> plugin;
    private CommandMap commandMap;

    public CommandRegistry(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        loadMap();
    }

    private void loadMap() {
        if (!(plugin.getServer().getPluginManager() instanceof SimplePluginManager)) {
            throw new IllegalStateException("Plugin Manager is not an instance of SimplePluginManager!");
        }
        SimplePluginManager pluginManager = (SimplePluginManager) plugin.getServer().getPluginManager();
        try {
            Field field = pluginManager.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.commandMap = (CommandMap) field.get(pluginManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new command. The Command object needs the {@link CommandData} annotation, to define some properties,
     * like name, permissions, etc.
     *
     * @param command The command to register.
     */
    public void registerCommand(Command command) {
        if (!command.getClass().isAnnotationPresent(CommandData.class)) {
            throw new IllegalArgumentException("Command " + command.getClass().getSimpleName() + " is missing the @CommandData annotation");
        }
        CommandData data = command.getClass().getAnnotation(CommandData.class);
        commandMap.register(data.name(), plugin.getName(), new org.bukkit.command.Command(data.name(), data.description(), data.usage(), Arrays.asList(data.aliases())) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (!data.permission().isEmpty() && !sender.hasPermission(data.permission())) {
                    String locale = "en_US";
                    if (sender instanceof Player)
                        locale = plugin.getUser((Player) sender).getLocale();
                    String customNoPermission = plugin.getLanguageHandler().translate(locale, NO_PERMISSION_KEY);
                    sender.sendMessage(customNoPermission.equals(NO_PERMISSION_KEY) ? NO_PERMISSION_FALLBACK : customNoPermission);
                    return true;
                }
                return command.onCommand(sender, this, commandLabel, args);
            }
        });
    }

}
