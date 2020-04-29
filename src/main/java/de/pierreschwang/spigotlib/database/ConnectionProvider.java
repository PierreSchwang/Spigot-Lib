package de.pierreschwang.spigotlib.database;

import java.sql.Connection;

/**
 * Provides the connection for the internal database api.
 */
public abstract class ConnectionProvider {

    private final String url;
    private final String username, password;

    public ConnectionProvider(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public abstract Connection getConnection();

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
