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
        inventory.getInventoryCloseListener().accept((Player) event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        SimpleInventory inventory = InventoryFactory.getInventories().get(event.getInventory());
        if (inventory == null)
            return;
        event.setCancelled(true);
        Consumer<InventoryClickEvent> listener = inventory.getClickHandlers().get(event.getSlot());
        if(listener == null)
            return;
        listener.accept(event);
    }

}
