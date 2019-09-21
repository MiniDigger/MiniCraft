package me.minidigger.miniserver.test.server;

import com.google.gson.internal.$Gson$Preconditions;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.desktop.PreferencesEvent;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import me.minidigger.miniserver.test.api.Player;
import me.minidigger.miniserver.test.api.Server;
import me.minidigger.miniserver.test.model.ChatPosition;
import me.minidigger.miniserver.test.model.Dimension;
import me.minidigger.miniserver.test.model.GameMode;
import me.minidigger.miniserver.test.model.Key;
import me.minidigger.miniserver.test.model.LevelType;
import me.minidigger.miniserver.test.model.Position;
import me.minidigger.miniserver.test.model.ServerStatusResponse;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketState;
import me.minidigger.miniserver.test.protocol.client.ClientLoginDisconnectPacket;
import me.minidigger.miniserver.test.protocol.client.ClientLoginEncryptionRequest;
import me.minidigger.miniserver.test.protocol.client.ClientLoginSuccess;
import me.minidigger.miniserver.test.protocol.client.ClientPlayChatMessage;
import me.minidigger.miniserver.test.protocol.client.ClientPlayJoinGame;
import me.minidigger.miniserver.test.protocol.client.ClientPlayPluginMessage;
import me.minidigger.miniserver.test.protocol.client.ClientPlayPositionAndLook;
import me.minidigger.miniserver.test.protocol.client.ClientStatusPongPacket;
import me.minidigger.miniserver.test.protocol.client.ClientStatusResponsePacket;
import me.minidigger.miniserver.test.protocol.server.ServerHandshakePacket;
import me.minidigger.miniserver.test.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.miniserver.test.protocol.server.ServerLoginStartPacket;
import me.minidigger.miniserver.test.protocol.server.ServerPlayChatMessage;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPluginMessagePacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPingPacket;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequestPacket;

public class MiniServerPacketHandler implements PacketHandler {

    private static final Logger log = LoggerFactory.getLogger(MiniServerPacketHandler.class);

    private KeyPair pair;

    private Server server;

    public MiniServerPacketHandler(Server server) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        pair = keyGen.generateKeyPair();

        this.server = server;
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
        connection.initPlayer(packet.getUsername());
        if (server.isOfflineMode()) {
            join(connection);
        } else {
            ClientLoginEncryptionRequest request = new ClientLoginEncryptionRequest();
            request.setServerId("MiniServer");
            request.setKey(pair.getPublic().getEncoded());
            request.setToken(new byte[]{0xB, 0xA, 0xB, 0xE});
            connection.sendPacket(request);
        }
    }

    @Override
    public void handle(MiniConnection connection, ServerLoginEncryptionResponse packet) {
        join(connection);
    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPluginMessagePacket packet) {
        Player player = connection.getPlayer();
        player.setBrand(new String(packet.getData(), StandardCharsets.UTF_8));
        log.info("Client brand of {} is {}", player.getName(), player.getBrand());
    }

    @Override
    public void handle(MiniConnection connection, ServerPlayChatMessage packet) {
        log.info("[CHAT] <{}> {}", connection.getPlayer().getName(), packet.getMessage());

        Component msg = TextComponent.builder("<" + connection.getPlayer().getName() + "> ").color(TextColor.WHITE)
                .append(packet.getMessage()).color(TextColor.GRAY).build();

        for (Player player : server.getPlayers()) {
            player.sendMessage(msg);
        }
    }

    private void join(MiniConnection connection) {
        ClientLoginSuccess loginSuccess = new ClientLoginSuccess();
        loginSuccess.setUsername(connection.getPlayer().getName());
        loginSuccess.setUuid(UUID.randomUUID());
        connection.sendPacket(loginSuccess);
        connection.setState(PacketState.PLAY);

        ClientPlayJoinGame joinGame = new ClientPlayJoinGame();
        joinGame.setEntityId(-1);
        joinGame.setGameMode(GameMode.CREATIVE);
        joinGame.setDimension(Dimension.OVERWORLD);
        joinGame.setMaxplayers(255);
        joinGame.setLevelType(LevelType.AMPLIFIED);
        joinGame.setViewDistance(32);
        joinGame.setReducedDebugInfo(false);
        connection.sendPacket(joinGame);

        ClientPlayPluginMessage brand = new ClientPlayPluginMessage();
        brand.setChannel(Key.of("brand"));
        brand.setData(server.getName().getBytes(StandardCharsets.UTF_8));
        connection.sendPacket(brand);

        ClientPlayPositionAndLook positionAndLook = new ClientPlayPositionAndLook();
        positionAndLook.setPosition(new Position(0, 0, 0));
        connection.sendPacket(positionAndLook);

        connection.getPlayer().setConnection(connection);
        server.getPlayers().add(connection.getPlayer());
    }
}
