package me.minidigger.minicraft.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.client.ClientStatusPong;
import me.minidigger.minicraft.protocol.handler.ClientPacketHandler;
import me.minidigger.minicraft.api.Client;
import me.minidigger.minicraft.protocol.PacketDirection;
import me.minidigger.minicraft.protocol.client.ClientStatusResponse;
import me.minidigger.minicraft.protocol.server.ServerStatusPing;

public class MiniClientPacketHandler extends ClientPacketHandler {

    private static final Logger log = LoggerFactory.getLogger(MiniClientPacketHandler.class);

    private Client client;

    public MiniClientPacketHandler(Client client) {
        this.client = client;
    }

    @Override
    public PacketDirection getDirection() {
        return PacketDirection.TO_CLIENT;
    }

    @Override
    public void handle(MiniConnection connection, ClientStatusResponse clientStatusResponse) {
        log.info("Got server status: {}", clientStatusResponse.getResponse());

        ServerStatusPing serverStatusPing = new ServerStatusPing();
        connection.sendPacket(serverStatusPing);
    }

    @Override
    public void handle(MiniConnection connection, ClientStatusPong clientStatusPong) {
        log.info("got ping!");
    }
}
