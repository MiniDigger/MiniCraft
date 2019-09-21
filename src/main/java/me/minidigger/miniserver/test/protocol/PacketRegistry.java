package me.minidigger.miniserver.test.protocol;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import me.minidigger.miniserver.test.protocol.server.ServerHandshakePacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPingPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequestPacket;

public class PacketRegistry {

    private Table<PacketState, Integer, Class<? extends Packet>> serverRegistry = HashBasedTable.create();
    private Table<PacketState, Integer, Class<? extends Packet>> clientRegistry = HashBasedTable.create();

    public void init() {
        // handshake
        register(PacketDirection.TO_SERVER, PacketState.HANDSHAKE, 0, ServerHandshakePacket.class);

        // status
        register(PacketDirection.TO_SERVER, PacketState.STATUS, 0, ServerStatusRequestPacket.class);
        register(PacketDirection.TO_SERVER, PacketState.STATUS, 1, ServerStatusPingPacket.class);
    }

    public void register(PacketDirection direction, PacketState state, int packetId, Class<? extends Packet> packetClass) {
        if (direction == PacketDirection.TO_SERVER) {
            serverRegistry.put(state, packetId, packetClass);
        } else {
            clientRegistry.put(state, packetId, packetClass);
        }
    }

    public Class<? extends Packet> getPacket(PacketDirection direction, PacketState state, int packetId) {
        if (direction == PacketDirection.TO_SERVER) {
            return serverRegistry.get(state, packetId);
        } else {
            return clientRegistry.get(state, packetId);
        }
    }
}
