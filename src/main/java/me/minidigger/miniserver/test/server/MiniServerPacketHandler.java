package me.minidigger.miniserver.test.server;

import com.google.gson.internal.$Gson$Preconditions;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.desktop.PreferencesEvent;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import me.minidigger.miniserver.test.model.ServerStatusResponse;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketState;
import me.minidigger.miniserver.test.protocol.client.ClientLoginDisconnectPacket;
import me.minidigger.miniserver.test.protocol.client.ClientLoginEncryptionRequest;
import me.minidigger.miniserver.test.protocol.client.ClientLoginSuccess;
import me.minidigger.miniserver.test.protocol.client.ClientStatusPongPacket;
import me.minidigger.miniserver.test.protocol.client.ClientStatusResponsePacket;
import me.minidigger.miniserver.test.protocol.server.ServerHandshakePacket;
import me.minidigger.miniserver.test.protocol.server.ServerLoginStartPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPingPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequestPacket;

public class MiniServerPacketHandler implements PacketHandler {

    private static final Logger log = LoggerFactory.getLogger(MiniServerPacketHandler.class);

    private KeyPair pair;

    public MiniServerPacketHandler() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        pair = keyGen.generateKeyPair();
    }

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

    @Override
    public void handle(MiniConnection connection, ServerLoginStartPacket packet) {
        log.info("{} is trying to login", packet.getUsername());
//        ClientLoginEncryptionRequest request = new ClientLoginEncryptionRequest();
//        request.setServerId("MiniServer");
//        request.setKey(pair.getPublic().getEncoded());
//        request.setToken(new byte[]{0xB, 0xA, 0xB, 0xE});
//        connection.sendPacket(request);

        ClientLoginSuccess loginSuccess = new ClientLoginSuccess();
        loginSuccess.setUsername(packet.getUsername());
        loginSuccess.setUuid(UUID.randomUUID());
        connection.sendPacket(loginSuccess);
        connection.setState(PacketState.PLAY);
    }
}
