package me.minidigger.miniserver.test.protocol;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

import me.minidigger.miniserver.test.protocol.client.ClientLoginDisconnectPacket;
import me.minidigger.miniserver.test.protocol.client.ClientLoginEncryptionRequest;
import me.minidigger.miniserver.test.protocol.client.ClientLoginSuccess;
import me.minidigger.miniserver.test.protocol.client.ClientPlayJoinGame;
import me.minidigger.miniserver.test.protocol.client.ClientPlayPluginMessage;
import me.minidigger.miniserver.test.protocol.client.ClientPlayPositionAndLook;
import me.minidigger.miniserver.test.protocol.client.ClientStatusPongPacket;
import me.minidigger.miniserver.test.protocol.client.ClientStatusResponsePacket;
import me.minidigger.miniserver.test.protocol.server.ServerHandshakePacket;
import me.minidigger.miniserver.test.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.miniserver.test.protocol.server.ServerLoginStartPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPingPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequestPacket;

public class PacketRegistry {

    private static final Logger log = LoggerFactory.getLogger(PacketRegistry.class);

    private Table<PacketState, Integer, Class<? extends Packet>> serverRegistry = HashBasedTable.create();
    private Table<PacketState, Integer, Class<? extends Packet>> clientRegistry = HashBasedTable.create();

    public void init() {
        //
        // SERVER
        //

        // handshake
        register(PacketDirection.TO_SERVER, PacketState.HANDSHAKE, 0, ServerHandshakePacket.class);

        // status
        register(PacketDirection.TO_SERVER, PacketState.STATUS, 0, ServerStatusRequestPacket.class);
        register(PacketDirection.TO_SERVER, PacketState.STATUS, 1, ServerStatusPingPacket.class);

        // login
        register(PacketDirection.TO_SERVER, PacketState.LOGIN, 0, ServerLoginStartPacket.class);
        register(PacketDirection.TO_SERVER, PacketState.LOGIN, 1, ServerLoginEncryptionResponse.class);

        //
        // CLIENT
        //

        // status
        register(PacketDirection.TO_CLIENT, PacketState.STATUS, 0, ClientStatusResponsePacket.class);
        register(PacketDirection.TO_CLIENT, PacketState.STATUS, 1, ClientStatusPongPacket.class);

        // login
        register(PacketDirection.TO_CLIENT, PacketState.LOGIN, 0, ClientLoginDisconnectPacket.class);
        register(PacketDirection.TO_CLIENT, PacketState.LOGIN, 1, ClientLoginEncryptionRequest.class);
        register(PacketDirection.TO_CLIENT, PacketState.LOGIN, 2, ClientLoginSuccess.class);

        // play
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x18, ClientPlayPluginMessage.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x25, ClientPlayJoinGame.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x35, ClientPlayPositionAndLook.class);
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

    public void fillInfo(Packet packet) {
        // this is really ugly, lol
        var cell = serverRegistry.cellSet().stream().filter(c -> Objects.equals(c.getValue(), packet.getClass())).findFirst();
        if(cell.isEmpty()) {
            cell = clientRegistry.cellSet().stream().filter(c -> Objects.equals(c.getValue(), packet.getClass())).findFirst();
        }
        cell.ifPresentOrElse((c -> {
            packet.setId(c.getColumnKey());
            packet.setState(c.getRowKey());
        }), () -> {
            log.warn("Could not fill info for packet {}", packet);
        });
    }
}
