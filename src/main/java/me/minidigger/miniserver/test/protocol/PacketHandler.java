package me.minidigger.miniserver.test.protocol;

import me.minidigger.miniserver.test.protocol.server.ServerHandshakePacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPingPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequestPacket;
import me.minidigger.miniserver.test.server.MiniConnection;

public interface PacketHandler {
    void handle(MiniConnection connection, ServerHandshakePacket packet);

    void handle(MiniConnection connection, ServerStatusRequestPacket packet);

    void handle(MiniConnection connection, ServerStatusPingPacket packet);
}
