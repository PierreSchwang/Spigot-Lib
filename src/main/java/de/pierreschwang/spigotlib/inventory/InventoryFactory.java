package de.pierreschwang.spigotlib.inventory;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class InventoryFactory {

    private static final Map<Inventory, SimpleInventory> inventories = new HashMap<>();

    public static SimpleInventory create(int size, String title) {
        SimpleInventory inventory = new SimpleInventory(size, title);
        inventories.put(inventory.getInventory(), inventory);
        return inventory;
    }

    public static SimpleInventory create(InventoryType type, String title) {
        SimpleInventory inventory = new SimpleInventory(type, title);
        inventories.put(inventory.getInventory(), inventory);
        return inventory;
    }

    public static SimplePaginatedInventory createPaginated(int size, String title, Integer... dynamicSlots) {
        SimplePaginatedInventory paginatedInventory = new SimplePaginatedInventory(size, title, dynamicSlots);
        inventories.put(paginatedInventory.getInventory(), paginatedInventory);
        return paginatedInventory;
    }

    static Map<Inventory, SimpleInventory> getInventories() {
        return inventories;
    }
}
