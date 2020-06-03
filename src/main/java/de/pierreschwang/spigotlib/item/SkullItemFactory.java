package de.pierreschwang.spigotlib.item;

import de.pierreschwang.spigotlib.nms.NmsHelper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullItemFactory extends ItemFactory<SkullMeta> {

    private static final Class<?> gameProfileClass = NmsHelper.getClass("com.mojang.authlib.GameProfile");
    private static final Field propertyMapField = NmsHelper.getField(gameProfileClass, "properties");
    private static Class<?> propertyClass = NmsHelper.getClass("com.mojang.authlib.properties.Property");
    private static Method putProperty = NmsHelper.getMethod(NmsHelper.getClass("com.mojang.authlib.properties.PropertyMap"), "put", Object.class, Object.class);

    public SkullItemFactory(ItemStack itemStack) {
        super(itemStack);
    }

    public SkullItemFactory owner(String owner) {
        getMeta().setOwner(owner);
        return this;
    }

    public SkullItemFactory texture(String texture) {
        try {
            final Object gameProfile = gameProfileClass.getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), null);
            final Object propertyMap = propertyMapField.get(gameProfile);
            final Object property = propertyClass.getConstructor(String.class, String.class).newInstance("textures", texture);
            putProperty.invoke(propertyMap, "textures", property);
            NmsHelper.setField(getMeta(), "profile", gameProfile);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this;
    }

}
