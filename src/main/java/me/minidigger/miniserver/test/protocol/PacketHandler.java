package me.minidigger.miniserver.test.protocol;

import me.minidigger.miniserver.test.protocol.server.ServerHandshake;
import me.minidigger.miniserver.test.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.miniserver.test.protocol.server.ServerLoginStart;
import me.minidigger.miniserver.test.protocol.server.ServerPlayBlockPlace;
import me.minidigger.miniserver.test.protocol.server.ServerPlayChatMessage;
import me.minidigger.miniserver.test.protocol.server.ServerPlayClientSettings;
import me.minidigger.miniserver.test.protocol.server.ServerPlayKeepAlive;
import me.minidigger.miniserver.test.protocol.server.ServerPlayLook;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPlayerAbilities;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPluginMessage;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPosition;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPositionAndLook;
import me.minidigger.miniserver.test.protocol.server.ServerPlayTeleportConfirm;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPing;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequest;
import me.minidigger.miniserver.test.server.MiniConnection;

public interface PacketHandler {

    PacketDirection getDirection();

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
