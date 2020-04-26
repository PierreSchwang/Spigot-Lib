package de.pierreschwang.spigotlib.hook;

import java.util.Collection;

public abstract class PluginHook<F extends PluginHookFunctionality> {

    private F implementation;

    public void init() {
        for (PluginHookImplementation<F> impl : getPossibleImplementations()) {
            if (impl.getPlugin() != null) {
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
