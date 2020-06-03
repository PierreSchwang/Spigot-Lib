package de.pierreschwang.spigotlib.nms.intercept;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

public interface PacketInterceptor {

    /**
     * Called as soon as one or more packets are received for a player. The packet name to be listened to is determined
     * in {@link PacketInterceptorRegistry#registerInterceptor(String, PacketInterceptor)}.
     *
     * @param player The player on which the interceptor is registered.
     * @param packet The packet which the player received.
     * @param ctx    The {@link ChannelHandlerContext} of the {@link Channel} on which the packet was
     *               received.
     */
    void onPacketReceive(Player player, Object packet, ChannelHandlerContext ctx);

}
