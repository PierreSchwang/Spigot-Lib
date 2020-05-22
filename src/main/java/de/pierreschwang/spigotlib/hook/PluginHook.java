package de.pierreschwang.spigotlib.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public abstract class PluginHook<F extends PluginHookFunctionality> {

    private F implementation;

    public void init() {
        for (PluginHookImplementation<F> impl : getPossibleImplementations()) {
            final Plugin plugin = Bukkit.getPluginManager().getPlugin(impl.getPluginName());
            if (plugin != null) {
                implementation = impl.getFunctionality();
                break;
            }
        }
        if (implementation != null)
            return;
        implementation = getFallback();
    }

    public abstract Collection<PluginHookImplementation<F>> getPossibleImplementations();

    public abstract F getFallback();

    public F getHook() {
        return implementation;
    }

}
