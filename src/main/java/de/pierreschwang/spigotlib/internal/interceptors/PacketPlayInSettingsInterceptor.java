package de.pierreschwang.spigotlib.internal.interceptors;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import de.pierreschwang.spigotlib.event.PlayerLocaleUpdateEvent;
import de.pierreschwang.spigotlib.event.PlayerReadyEvent;
import de.pierreschwang.spigotlib.nms.NmsHelper;
import de.pierreschwang.spigotlib.nms.intercept.PacketInterceptor;
import de.pierreschwang.spigotlib.user.User;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketPlayInSettingsInterceptor implements PacketInterceptor {

    private final AbstractJavaPlugin<?> plugin;
    private Field languageField;

    public PacketPlayInSettingsInterceptor(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        Class<?> packetPlayInSettings = NmsHelper.getNmsClass("PacketPlayInSettings");
        for (Field field : packetPlayInSettings.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(String.class)) {
                this.languageField = field;
                languageField.setAccessible(true);
                break;
            }
        }
    }

    @Override
    public void onPacketReceive(Player player, Object packet, ChannelHandlerContext ctx) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                String locale = (String) languageField.get(packet);
                User user = plugin.getUser(player);
                if (user.getLocale() == null) {
                    user.setLocale(locale);
                    Bukkit.getPluginManager().callEvent(new PlayerReadyEvent<>(user, player));
                    return;
                }
                if (!locale.equals(user.getLocale()))
                    Bukkit.getPluginManager().callEvent(new PlayerLocaleUpdateEvent(player, user.getLocale(), locale));
                user.setLocale(locale);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}