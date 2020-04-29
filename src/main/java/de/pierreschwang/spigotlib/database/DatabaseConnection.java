package de.pierreschwang.spigotlib.database;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class DatabaseConnection {

    private AbstractJavaPlugin<?> plugin;
    private ConnectionProvider provider;

    public DatabaseConnection(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        if (plugin.getConnectionProvider() == null) {
            System.out.println("Disabling database support");
            return;
        }
        final String url = "";
        final String username = "";
        final String password = "";
        try {
            provider = plugin.getConnectionProvider()
                .getConstructor(String.class, String.class, String.class)
                .newInstance(url, username, password);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return provider.getConnection();
    }

    public ConnectionProvider getProvider() {
        return provider;
    }

    public AbstractJavaPlugin<?> getPlugin() {
        return plugin;
    }
}
