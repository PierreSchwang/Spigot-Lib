package de.pierreschwang.spigotlib.hook;

import org.bukkit.plugin.Plugin;

public abstract class PluginHookImplementation<T extends PluginHookFunctionality> {

    private final Plugin plugin;

    protected PluginHookImplementation(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public abstract T getFunctionality();

}