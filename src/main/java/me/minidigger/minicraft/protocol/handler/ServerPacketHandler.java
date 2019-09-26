package me.minidigger.minicraft.protocol.handler;

import me.minidigger.minicraft.protocol.client.ClientStatusPong;
import me.minidigger.minicraft.protocol.client.ClientStatusResponse;
import me.minidigger.minicraft.netty.MiniConnection;

public abstract class ServerPacketHandler implements PacketHandler {

    @Override
    public void handle(MiniConnection connection, ClientStatusResponse clientStatusResponse) {

    }

    @Override
    public void handle(MiniConnection connection, ClientStatusPong clientStatusPong) {

    }
}
