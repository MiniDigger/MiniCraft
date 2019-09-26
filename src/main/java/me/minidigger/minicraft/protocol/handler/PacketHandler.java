package me.minidigger.minicraft.protocol.handler;

import me.minidigger.minicraft.protocol.client.ClientStatusPong;
import me.minidigger.minicraft.protocol.server.ServerHandshake;
import me.minidigger.minicraft.protocol.PacketDirection;
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
import me.minidigger.minicraft.netty.MiniConnection;

public interface PacketHandler {

    PacketDirection getDirection();

    // client
    void handle(MiniConnection connection, ClientStatusResponse clientStatusResponse);

    void handle(MiniConnection connection, ClientStatusPong clientStatusPong);

    // server

    void handle(MiniConnection connection, ServerHandshake packet);

    void handle(MiniConnection connection, ServerStatusRequest packet);

    void handle(MiniConnection connection, ServerStatusPing packet);

    void handle(MiniConnection connection, ServerLoginStart packet);

    void handle(MiniConnection connection, ServerLoginEncryptionResponse packet);

    void handle(MiniConnection connection, ServerPlayPluginMessage packet);

    void handle(MiniConnection connection, ServerPlayChatMessage packet);

    void handle(MiniConnection connection, ServerPlayKeepAlive serverPlayKeepAlive);

    void handle(MiniConnection connection, ServerPlayClientSettings serverPlayClientSettings);

    void handle(MiniConnection connection, ServerPlayTeleportConfirm serverPlayTeleportConfirm);

    void handle(MiniConnection connection, ServerPlayPositionAndLook serverPlayPositionAndLook);

    void handle(MiniConnection connection, ServerPlayPosition serverPlayPosition);

    void handle(MiniConnection connection, ServerPlayLook serverPlayLook);

    void handle(MiniConnection connection, ServerPlayPlayerAbilities serverPlayPlayerAbilities);

    void handle(MiniConnection connection, ServerPlayBlockPlace serverPlayBlockPlace);
}
