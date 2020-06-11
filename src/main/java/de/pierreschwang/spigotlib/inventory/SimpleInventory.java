package de.pierreschwang.spigotlib.inventory;

import de.pierreschwang.spigotlib.item.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleInventory {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickHandlers = new HashMap<>();
    private Consumer<Player> inventoryCloseListener = (player) -> {
    };

    SimpleInventory(int size, String title) {
        inventory = Bukkit.createInventory(null, size, title);
    }

    SimpleInventory(int size) {
        inventory = Bukkit.createInventory(null, size);
    }

    SimpleInventory(InventoryType type) {
        inventory = Bukkit.createInventory(null, type);
    }

    SimpleInventory(InventoryType type, String title) {
        inventory = Bukkit.createInventory(null, type, title);
    }

    public SimpleInventory setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        this.inventory.setItem(slot, item);
        this.clickHandlers.put(slot, eventConsumer);
        return this;
    }

    public SimpleInventory setItem(int slot, ItemFactory<?> item, Consumer<InventoryClickEvent> eventConsumer) {
        return setItem(slot, item.apply(), eventConsumer);
    }

    // PacketPlayOutOpenWindow

    public SimpleInventory setItem(int slot, ItemStack item) {
        return setItem(slot, item, (ev) -> {
        });
    }

    public SimpleInventory setItem(int slot, ItemFactory<?> item) {
        return setItem(slot, item, (ev) -> {
        });
    }

    public SimpleInventory fill(int beginning, int end, ItemStack item) {
        for (int i = beginning; i < end; i++) {
            setItem(i, item);
        }
        return this;
    }

    public SimpleInventory fill(int beginning, int end, ItemFactory<?> item) {
        return fill(beginning, end, item.apply());
    }

    public SimpleInventory setCloseListener(Consumer<Player> eventConsumer) {
        this.inventoryCloseListener = eventConsumer;
        return this;
    }

    public void open(Player... players) {
        for (Player player : players) {
            player.openInventory(inventory);
        }
    }

    Inventory getInventory() {
        return inventory;
    }

    Map<Integer, Consumer<InventoryClickEvent>> getClickHandlers() {
        return clickHandlers;
    }

    Consumer<Player> getInventoryCloseListener() {
        return inventoryCloseListener;
    }

}