package de.pierreschwang.spigotlib.internal;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import de.pierreschwang.spigotlib.event.PlayerLocaleUpdateEvent;
import de.pierreschwang.spigotlib.event.PlayerReadyEvent;
import de.pierreschwang.spigotlib.nms.NmsHelper;
import de.pierreschwang.spigotlib.user.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class SettingsPacketInterceptor {

    private final AbstractJavaPlugin<?> plugin;
    private Field channelField;
    private Field languageField;

    public SettingsPacketInterceptor(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        // Channel field name is different in many versions...
        Class<?> networkManager = NmsHelper.getNmsClass("NetworkManager");
        for (Field field : networkManager.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(Channel.class)) {
                this.channelField = field;
                channelField.setAccessible(true);
                break;
            }
        }
        Class<?> packetPlayInSettings = NmsHelper.getNmsClass("PacketPlayInSettings");
        for (Field field : packetPlayInSettings.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(String.class)) {
                this.languageField = field;
                languageField.setAccessible(true);
                break;
            }
        }
    }

    public void register(Player player) {
        Channel channel = getNetworkChannel(player);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("decoder", "settingsPacketListener", new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                if (!packet.getClass().getSimpleName().equals("PacketPlayInSettings")) {
                    super.channelRead(channelHandlerContext, packet);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        String locale = (String) languageField.get(packet);
                        User user = plugin.getUser(player);
                        if (user.getLocale() == null) {
                            user.setLocale(locale);
                            Bukkit.getPluginManager().callEvent(new PlayerReadyEvent<>(user, player));
                            super.channelRead(channelHandlerContext, packet);
                            return;
                        }
                        if (!locale.equals(user.getLocale()))
                            Bukkit.getPluginManager().callEvent(new PlayerLocaleUpdateEvent(player, user.getLocale(), locale));
                        user.setLocale(locale);
                        super.channelRead(channelHandlerContext, packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void unregister(Player player) {
        Channel channel = getNetworkChannel(player);
        if (channel == null || channel.pipeline() == null)
            return;
        if (channel.pipeline().get("settingsPacketListener") != null)
            channel.pipeline().remove("settingsPacketListener");
    }

    private Channel getNetworkChannel(Player player) {
        Object networkManager = getNetworkManager(player);
        try {
            return (Channel) channelField.get(networkManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getNetworkManager(Player player) {
        try {
            Object playerHandle = NmsHelper.playerHandle(player);
            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            return playerConnection.getClass().getField("networkManager").get(playerConnection);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
