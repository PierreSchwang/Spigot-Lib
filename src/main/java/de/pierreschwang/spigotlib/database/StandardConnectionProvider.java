package de.pierreschwang.spigotlib.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Standard implementation for {@link ConnectionProvider} without connection pooling or other features. May be useful
 * for short life applications, but generally not recommended for production. A better option would be a custom
 * connection provider with a hikari cp implementation (e.g.)
 */
public class StandardConnectionProvider extends ConnectionProvider {

    private Connection connection;

    public StandardConnectionProvider(String url, String username, String password) {
        super(url + "?autoReconnect=true", username, password);
        reconnect();
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                reconnect();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    private void reconnect() {
        try {
            this.connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
