package de.pierreschwang.spigotlib.inventory;

import de.pierreschwang.spigotlib.inventory.exceptions.PageIndexOutOfBoundsException;
import de.pierreschwang.spigotlib.item.ItemFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimplePaginatedInventory extends SimpleInventory {
    private final LinkedHashMap<Integer, ItemStack> inventoryContents;
    private int currentPage;

    SimplePaginatedInventory(int size, String title) {
        super(size, title);
        inventoryContents = new LinkedHashMap<>();
        currentPage = 1;
    }

    SimplePaginatedInventory(int size) {
        this(size, "Inventory");
    }

    public LinkedHashMap<Integer, ItemStack> getInventoryContents() {
        return inventoryContents;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPage() {
        int highestItemSlot = getMaxItemIndex();
        return (int) Math.ceil((highestItemSlot + 1) / (float) getInventory().getSize());
    }

    public int getMaxItemIndex() {
        OptionalInt optionalHighestItemSlot = getInventoryContents().keySet().stream().mapToInt(Integer::intValue).max();
        if(!optionalHighestItemSlot.isPresent())
            return -1;
        return optionalHighestItemSlot.getAsInt();
    }

    public int getOffsetForPage(int page) {
        return (page - 1) * getInventory().getSize();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public SimplePaginatedInventory setItem(int page, int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        if(page < 1) {
            throw new PageIndexOutOfBoundsException("Page number can not be less than 1");
        }
        int targetSlot = slot + getOffsetForPage(page);
        this.inventoryContents.put(targetSlot, item);
        super.getClickHandlers().put(targetSlot, eventConsumer);
        return this;
    }

    @Override
    public SimplePaginatedInventory setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        return this.setItem(1, slot, item, eventConsumer);
    }

    @Override
    public SimplePaginatedInventory setItem(int slot, ItemFactory<?> item, Consumer<InventoryClickEvent> eventConsumer) {
        return this.setItem(slot, item.apply(), eventConsumer);
    }

    @Override
    public SimplePaginatedInventory setItem(int slot, ItemStack item) {
        return this.setItem(slot, item, event -> {});
    }

    @Override
    public SimplePaginatedInventory setItem(int slot, ItemFactory<?> item) {
        return this.setItem(slot, item.apply(), event -> {});
    }

    public SimplePaginatedInventory addItem(ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        int maxSlot = getMaxItemIndex();
        for(int index = 0; index < maxSlot; index++) {
            if(getInventoryContents().get(index) == null) {
                return this.setItem(index, item, eventConsumer);
            }
        }
        return this.setItem(maxSlot + 1, item, eventConsumer);
    }

    public SimplePaginatedInventory addItem(ItemFactory<?> item, Consumer<InventoryClickEvent> eventConsumer) {
        return this.addItem(item.apply(), eventConsumer);
    }

    public SimplePaginatedInventory addItem(ItemStack item) {
        return this.addItem(item, event -> {});
    }

    public SimplePaginatedInventory addItem(ItemFactory<?> item) {
        return this.addItem(item.apply(), event -> {});
    }

    public void open(int page, Player... players) {
        if(page < 0) {
            throw new PageIndexOutOfBoundsException("Page number can not be less than 1");
        }
        setCurrentPage(page);
        int offset = getOffsetForPage(page);
        for(int index = offset; index < offset + getInventory().getSize(); index++) {
            getInventory().setItem(index - offset, getInventoryContents().get(index));
        }
        for (Player player : players) {
            player.openInventory(getInventory());
        }
    }

    @Override
    public void open(Player... players) {
        this.open(1, players);
    }
}
