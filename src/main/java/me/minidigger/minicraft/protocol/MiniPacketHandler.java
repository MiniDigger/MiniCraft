package me.minidigger.minicraft.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import me.minidigger.minicraft.protocol.MiniPacket;
import me.minidigger.minicraft.protocol.PacketDirection;
import me.minidigger.minicraft.netty.MiniConnection;

public abstract class MiniPacketHandler {

    private Map<Class<? extends MiniPacket>, List<BiConsumer<MiniConnection, MiniPacket>>> handlers = new HashMap<>();

    public <T extends MiniPacket> void registerCallback(Class<T> packet, BiConsumer<MiniConnection, T> callback) {
        //noinspection unchecked
        handlers.computeIfAbsent(packet, (key) -> new ArrayList<>()).add((BiConsumer<MiniConnection, MiniPacket>) callback);
    }

    public <T extends MiniPacket> void handle(MiniConnection connection, T packet) {
        handlers.getOrDefault(packet.getClass(), new ArrayList<>()).forEach(consumer -> {
            consumer.accept(connection, packet);
        });
    }

    public abstract PacketDirection getDirection();

    public abstract void init();
}
