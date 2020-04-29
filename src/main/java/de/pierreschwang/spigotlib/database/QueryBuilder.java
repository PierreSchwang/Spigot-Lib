package de.pierreschwang.spigotlib.database;

import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QueryBuilder {

    private String table;
    private QueryAction action;
    private String[] columns;
    private Object[] params;

    public QueryBuilder table(String table) {
        this.table = table;
        return this;
    }

    public QueryBuilder action(QueryAction action) {
        this.action = action;
        return this;
    }

    public QueryBuilder columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public QueryBuilder params(Object... params) {
        this.params = params;
        return this;
    }

    public void executeQuery(DatabaseConnection connection, Consumer<List<Row>> result) {
        Bukkit.getScheduler().runTaskAsynchronously(connection.getPlugin(), () -> {
            try (final PreparedStatement statement = buildStatement(connection)) {
                if(statement == null)
                    return;
                final ResultSet resultSet = statement.executeQuery();
                ResultSetMetaData data = resultSet.getMetaData();
                final List<Row> rows = new ArrayList<>();
                while(resultSet.next()) {
                    Row row = new Row();
                    for (int i = 0; i < data.getColumnCount(); i++) {
                        String name = data.getColumnName(i);
                        row.getEntries().put(name, new RowEntry(resultSet.getObject(name)));
                    }
                    rows.add(row);
                }
                result.accept(rows);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void executeUpdate(DatabaseConnection connection) {
        Bukkit.getScheduler().runTaskAsynchronously(connection.getPlugin(), () -> {
            try (final PreparedStatement statement = buildStatement(connection)) {
                if(statement == null)
                    return;
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    private PreparedStatement buildStatement(DatabaseConnection connection) {
        try {
            final PreparedStatement statement = connection.getConnection().prepareStatement(
                String.format(action.getQuery(), table)
            );
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
