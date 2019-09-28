package me.minidigger.minicraft.protocol;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import me.minidigger.minicraft.protocol.client.ClientPlayDeclareCommands;
import me.minidigger.minicraft.protocol.client.ClientStatusPong;
import me.minidigger.minicraft.protocol.server.ServerHandshake;
import me.minidigger.minicraft.protocol.client.ClientLoginDisconnect;
import me.minidigger.minicraft.protocol.client.ClientLoginEncryptionRequest;
import me.minidigger.minicraft.protocol.client.ClientLoginSuccess;
import me.minidigger.minicraft.protocol.client.ClientPlayChatMessage;
import me.minidigger.minicraft.protocol.client.ClientPlayChunkData;
import me.minidigger.minicraft.protocol.client.ClientPlayJoinGame;
import me.minidigger.minicraft.protocol.client.ClientPlayKeepAlive;
import me.minidigger.minicraft.protocol.client.ClientPlayPluginMessage;
import me.minidigger.minicraft.protocol.client.ClientPlayPositionAndLook;
import me.minidigger.minicraft.protocol.client.ClientStatusResponse;
import me.minidigger.minicraft.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.minicraft.protocol.server.ServerLoginStart;
import me.minidigger.minicraft.protocol.server.ServerPlayBlockPlace;
import me.minidigger.minicraft.protocol.server.ServerPlayChatMessage;
import me.minidigger.minicraft.protocol.server.ServerPlayClientSettings;
import me.minidigger.minicraft.protocol.server.ServerPlayKeepAlive;
import me.minidigger.minicraft.protocol.server.ServerPlayLook;
import me.minidigger.minicraft.protocol.server.ServerPlayPlayerAbilities;
import me.minidigger.minicraft.protocol.server.ServerPlayPluginMessage;
import me.minidigger.minicraft.protocol.server.ServerPlayPosition;
import me.minidigger.minicraft.protocol.server.ServerPlayPositionAndLook;
import me.minidigger.minicraft.protocol.server.ServerPlayTeleportConfirm;
import me.minidigger.minicraft.protocol.server.ServerStatusPing;
import me.minidigger.minicraft.protocol.server.ServerStatusRequest;

public class MiniPacketRegistry {

    private static final Logger log = LoggerFactory.getLogger(MiniPacketRegistry.class);

    private Table<PacketState, Integer, Class<? extends MiniPacket>> serverRegistry = HashBasedTable.create();
    private Table<PacketState, Integer, Class<? extends MiniPacket>> clientRegistry = HashBasedTable.create();

    public void init() {
        //
        // SERVER
        //

        // handshake
        register(PacketDirection.TO_SERVER, PacketState.HANDSHAKE, 0, ServerHandshake.class);

        // status
        register(PacketDirection.TO_SERVER, PacketState.STATUS, 0, ServerStatusRequest.class);
        register(PacketDirection.TO_SERVER, PacketState.STATUS, 1, ServerStatusPing.class);

        // login
        register(PacketDirection.TO_SERVER, PacketState.LOGIN, 0, ServerLoginStart.class);
        register(PacketDirection.TO_SERVER, PacketState.LOGIN, 1, ServerLoginEncryptionResponse.class);

        // play
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x00, ServerPlayTeleportConfirm.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x03, ServerPlayChatMessage.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x05, ServerPlayClientSettings.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x0B, ServerPlayPluginMessage.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x0F, ServerPlayKeepAlive.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x11, ServerPlayPosition.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x12, ServerPlayPositionAndLook.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x13, ServerPlayLook.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x19, ServerPlayPlayerAbilities.class);
        register(PacketDirection.TO_SERVER, PacketState.PLAY, 0x2C, ServerPlayBlockPlace.class);

        //
        // CLIENT
        //

        // status
        register(PacketDirection.TO_CLIENT, PacketState.STATUS, 0, ClientStatusResponse.class);
        register(PacketDirection.TO_CLIENT, PacketState.STATUS, 1, ClientStatusPong.class);

        // login
        register(PacketDirection.TO_CLIENT, PacketState.LOGIN, 0, ClientLoginDisconnect.class);
        register(PacketDirection.TO_CLIENT, PacketState.LOGIN, 1, ClientLoginEncryptionRequest.class);
        register(PacketDirection.TO_CLIENT, PacketState.LOGIN, 2, ClientLoginSuccess.class);

        // play
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x0E, ClientPlayChatMessage.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x11, ClientPlayDeclareCommands.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x18, ClientPlayPluginMessage.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x20, ClientPlayKeepAlive.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x21, ClientPlayChunkData.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x25, ClientPlayJoinGame.class);
        register(PacketDirection.TO_CLIENT, PacketState.PLAY, 0x35, ClientPlayPositionAndLook.class);
    }

    public void register(PacketDirection direction, PacketState state, int packetId, Class<? extends MiniPacket> packetClass) {
        if (direction == PacketDirection.TO_SERVER) {
            serverRegistry.put(state, packetId, packetClass);
        } else {
            clientRegistry.put(state, packetId, packetClass);
        }
    }

    public Class<? extends MiniPacket> getPacket(PacketDirection direction, PacketState state, int packetId) {
        if (direction == PacketDirection.TO_SERVER) {
            return serverRegistry.get(state, packetId);
        } else {
            return clientRegistry.get(state, packetId);
        }
    }

    public void fillInfo(MiniPacket packet) {
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
