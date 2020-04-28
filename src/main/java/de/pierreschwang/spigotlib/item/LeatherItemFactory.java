package de.pierreschwang.spigotlib.item;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherItemFactory extends ItemFactory<LeatherArmorMeta> {

    public LeatherItemFactory(ItemStack itemStack) {
        super(itemStack);
    }

    public LeatherItemFactory dye(Color color) {
        getMeta().setColor(color);
        return this;
    }

}