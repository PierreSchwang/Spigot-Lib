package de.pierreschwang.spigotlib.database;

import java.util.HashMap;
import java.util.Map;

public class Row {

    private final Map<String, RowEntry> entries = new HashMap<>();

    public RowEntry get(String name) {
        return entries.get(name);
    }

    public Map<String, RowEntry> getEntries() {
        return entries;
    }

}