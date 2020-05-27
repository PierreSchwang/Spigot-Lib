package de.pierreschwang.spigotlib.nms.intercept;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PacketInterceptorRegistry {

    private static final Set<PacketInterceptor> EMPTY_SET = new HashSet<>();
    private final Map<String, Set<PacketInterceptor>> interceptors = new HashMap<>();

    public PacketInterceptorRegistry() {
    }

    /**
     * Register a new {@link PacketInterceptor} to a specific packet.
     *
     * @param packetName The handler.
     * @param handler    the packet to listen on.
     */
    public void registerInterceptor(String packetName, PacketInterceptor handler) {
        interceptors.computeIfAbsent(packetName, s -> new HashSet<>()).add(handler);
    }

    public Set<PacketInterceptor> getInterceptors(String packet) {
        return interceptors.getOrDefault(packet, EMPTY_SET);
    }

    public Map<String, Set<PacketInterceptor>> getInterceptors() {
        return interceptors;
    }

}
