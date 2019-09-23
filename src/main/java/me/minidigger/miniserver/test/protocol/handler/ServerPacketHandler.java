package me.minidigger.miniserver.test.protocol.handler;

import me.minidigger.miniserver.test.protocol.client.ClientStatusPong;
import me.minidigger.miniserver.test.protocol.client.ClientStatusResponse;
import me.minidigger.miniserver.test.netty.MiniConnection;

public abstract class ServerPacketHandler implements PacketHandler {

    @Override
    public void handle(MiniConnection connection, ClientStatusResponse clientStatusResponse) {

    }

    @Override
    public void handle(MiniConnection connection, ClientStatusPong clientStatusPong) {

    }
}
