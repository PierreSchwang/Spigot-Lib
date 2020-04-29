package de.pierreschwang.spigotlib.database;

import java.util.UUID;
import java.util.function.Function;

public class RowEntry {

    private final Object object;

    public RowEntry(Object object) {
        this.object = object;
    }

    public String asString() {
        return String.valueOf(object);
    }

    public Integer asInt() {
        return Integer.valueOf(asString());
    }

    public Long asLong() {
        return Long.valueOf(asString());
    }

    public UUID asUuid() {
        return UUID.fromString(asString());
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean(asString());
    }

    public <T> T map(Function<RowEntry, T> mapper) {
        return mapper.apply(this);
    }

}
