package me.minidigger.miniserver.test.server;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import me.minidigger.miniserver.test.model.ServerStatusResponse;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.protocol.client.ClientStatusPongPacket;
import me.minidigger.miniserver.test.protocol.client.ClientStatusResponsePacket;
import me.minidigger.miniserver.test.protocol.server.ServerHandshakePacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPingPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequestPacket;

public class MiniServerPacketHandler implements PacketHandler {

    private static final Logger log = LoggerFactory.getLogger(MiniServerPacketHandler.class);

    @Override
    public void handle(MiniConnection connection, ServerHandshakePacket packet) {
        connection.setState(packet.getNextState());
        log.info("Advancing state to {}", packet.getNextState());
    }

    @Override
    public void handle(MiniConnection connection, ServerStatusRequestPacket packet) {
        ServerStatusResponse response = new ServerStatusResponse(
                new ServerStatusResponse.Version("HeyJA", 498),
                new ServerStatusResponse.Players(255, 0, List.of(
                        new ServerStatusResponse.Players.Player("Test", UUID.randomUUID()))),
                TextComponent.builder("Test.").color(TextColor.RED).append(" Test2").color(TextColor.GREEN).build(),
                null);

        ClientStatusResponsePacket responsePacket = new ClientStatusResponsePacket();
        responsePacket.setResponse(response);

        connection.sendPacket(responsePacket);
    }

    @Override
    public void handle(MiniConnection connection, ServerStatusPingPacket packet) {
        ClientStatusPongPacket response = new ClientStatusPongPacket();
        response.setPayload(packet.getPayload());
        connection.sendPacket(response);
    }
}
