package de.pierreschwang.spigotlib.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemFactory<T extends ItemMeta> {

    private final ItemStack itemStack;
    private final T meta;

    public ItemFactory(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = (T) itemStack.getItemMeta();
    }

    public static ItemFactory<ItemMeta> create(Material material) {
        return new ItemFactory<>(new ItemStack(material));
    }

    public static ItemFactory<ItemMeta> create(Material material, short durability) {
        return new ItemFactory<>(new ItemStack(material, 1, durability));
    }

    public static LeatherItemFactory leather(Material material) {
        if (!material.name().startsWith("LEATHER_"))
            throw new IllegalArgumentException("leather() must be called with a valid leather armor part!");
        return new LeatherItemFactory(new ItemStack(material));
    }

    public static SkullItemFactory skull() {
        return new SkullItemFactory(new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
    }

    public static FireworkChargeItemFactory fireworkCharge() {
        return new FireworkChargeItemFactory(new ItemStack(Material.FIREWORK_CHARGE));
    }

    public ItemFactory<T> amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemFactory<T> name(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemFactory<T> lore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public ItemFactory<T> lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemFactory<T> lore(String lore) {
        return lore(lore.split("\n"));
    }

    public ItemFactory<T> enchant(Enchantment enchantment, int lvl) {
        getMeta().addEnchant(enchantment, lvl, true);
        return this;
    }

    public ItemFactory<T> flag(ItemFlag... flags) {
        getMeta().addItemFlags(flags);
        return this;
    }

    public ItemStack apply() {
        this.itemStack.setItemMeta(meta);
        return this.itemStack;
    }

    T getMeta() {
        return meta;
    }
}
