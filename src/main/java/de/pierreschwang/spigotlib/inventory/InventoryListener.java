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
        if (inventory.getInventoryCloseListener() != null)
            inventory.getInventoryCloseListener().accept((Player) event.getPlayer());
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
            if(event.getSlot() == paginatedInventory.getPageSwitcherBackSlot()) {
                paginatedInventory.refresh(paginatedInventory.getPreviousPage(), (Player) event.getWhoClicked());
                return;
            }
            if(event.getSlot() == paginatedInventory.getPageSwitcherForwardSlot()) {
                paginatedInventory.refresh(paginatedInventory.getNextPage(), (Player) event.getWhoClicked());
                return;
            }
            int targetSlot = event.getSlot() + paginatedInventory.getOffsetForPage(paginatedInventory.getCurrentPage());
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