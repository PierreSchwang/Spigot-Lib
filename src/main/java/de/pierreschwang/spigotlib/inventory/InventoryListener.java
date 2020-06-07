package de.pierreschwang.spigotlib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.function.Consumer;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        SimpleInventory inventory = InventoryFactory.getInventories().get(event.getInventory());
        if (inventory == null)
            return;
        if (inventory.getInventoryCloseListener() == null)
            return;
        InventoryFactory.getInventories().remove(event.getInventory());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        SimpleInventory inventory = InventoryFactory.getInventories().get(event.getInventory());
        if (inventory == null)
            return;
        event.setCancelled(true);
        if(event.getClickedInventory() == event.getWhoClicked().getInventory())
            return;
        if(inventory instanceof SimplePaginatedInventory) {
            SimplePaginatedInventory paginatedInventory = (SimplePaginatedInventory) inventory;
            int targetSlot = event.getSlot() + ((paginatedInventory.getCurrentPage() - 1) * paginatedInventory.getInventory().getSize());
            Consumer<InventoryClickEvent> listener = inventory.getClickHandlers().get(targetSlot);
            if(listener == null)
                return;
            listener.accept(event);
        } else {
            Consumer<InventoryClickEvent> listener = inventory.getClickHandlers().get(event.getSlot());
            if(listener == null)
                return;
            listener.accept(event);
        }
    }
}