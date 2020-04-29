package de.pierreschwang.spigotlib.inventory;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

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

    static Map<Inventory, SimpleInventory> getInventories() {
        return inventories;
    }
}
