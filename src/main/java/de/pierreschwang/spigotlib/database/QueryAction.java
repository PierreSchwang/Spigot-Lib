package de.pierreschwang.spigotlib.database;

public enum QueryAction {

    INSERT("INSERT INTO %s "),
    UPDATE("UPDATE %s SET"),
    DELETE("DELETE %s FROM %s");

    private final String query;

    QueryAction(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}