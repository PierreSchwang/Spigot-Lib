package de.pierreschwang.spigotlib.hook;

public abstract class PluginHookImplementation<T extends PluginHookFunctionality> {

    private final String pluginName;

    protected PluginHookImplementation(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public abstract T getFunctionality();

}