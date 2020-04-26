package de.pierreschwang.spigotlib.command;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandRegistry {

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

    public void registerCommand(Command command) {
        if (!command.getClass().isAnnotationPresent(CommandData.class)) {
            throw new IllegalArgumentException("Command " + command.getClass().getSimpleName() + " is missing the @CommandData annotation");
        }
        CommandData data = command.getClass().getAnnotation(CommandData.class);
        commandMap.register(data.name(), plugin.getName(), new org.bukkit.command.Command(data.name(), data.description(), data.usage(), Arrays.asList(data.aliases())) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (!data.permission().isEmpty() && !sender.hasPermission(data.permission())) {
                    sender.sendMessage("§cYou dont have permission to do that!");
                    return true;
                }
                return command.onCommand(sender, this, commandLabel, args);
            }
        });
    }

}
