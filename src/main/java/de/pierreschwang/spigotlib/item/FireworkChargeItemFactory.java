package de.pierreschwang.spigotlib.item;

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

public class FireworkChargeItemFactory extends ItemFactory<FireworkEffectMeta> {

    public FireworkChargeItemFactory(ItemStack itemStack) {
        super(itemStack);
    }

    public FireworkChargeItemFactory effect(FireworkEffect fireworkEffect) {
        getMeta().setEffect(fireworkEffect);
        return this;
    }
}
