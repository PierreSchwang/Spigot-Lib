package de.pierreschwang.spigotlib.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullItemFactory extends ItemFactory<SkullMeta> {

    public SkullItemFactory(ItemStack itemStack) {
        super(itemStack);
    }

    public SkullItemFactory owner(String owner) {
        getMeta().setOwner(owner);
        return this;
    }

}
