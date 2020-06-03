package de.pierreschwang.spigotlib.internal;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import de.pierreschwang.spigotlib.nms.NmsHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PlayerPacketInterceptor {

    private final AbstractJavaPlugin<?> plugin;
    private Field channelField;

    public PlayerPacketInterceptor(AbstractJavaPlugin<?> plugin) {
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
    }

    public void register(Player player) {
        Channel channel = getNetworkChannel(player);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("decoder", "packetInterceptorListener", new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                String packetName = packet.getClass().getSimpleName();
                plugin.getPacketInterceptorRegistry().getInterceptors(packetName)
                    .forEach(interceptor -> interceptor.onPacketReceive(player, packet, channelHandlerContext));
                super.channelRead(channelHandlerContext, packet);
            }
        });
    }

    public void unregister(Player player) {
        Channel channel = getNetworkChannel(player);
        if (channel == null || channel.pipeline() == null)
            return;
        if (channel.pipeline().get("packetInterceptorListener") != null)
            channel.pipeline().remove("packetInterceptorListener");
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
