package me.minidigger.minicraft.server;

import net.kyori.nbt.ListTag;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import me.minidigger.minicraft.protocol.client.ClientStatusPong;
import me.minidigger.minicraft.protocol.handler.ServerPacketHandler;
import me.minidigger.minicraft.protocol.server.ServerHandshake;
import me.minidigger.minicraft.api.Player;
import me.minidigger.minicraft.api.Server;
import me.minidigger.minicraft.model.chunk.ChunkData;
import me.minidigger.minicraft.model.chunk.ChunkPosition;
import me.minidigger.minicraft.model.Dimension;
import me.minidigger.minicraft.model.GameMode;
import me.minidigger.minicraft.model.Key;
import me.minidigger.minicraft.model.LevelType;
import me.minidigger.minicraft.model.Position;
import me.minidigger.minicraft.model.ServerStatusResponse;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.PacketDirection;
import me.minidigger.minicraft.protocol.PacketState;
import me.minidigger.minicraft.protocol.client.ClientLoginEncryptionRequest;
import me.minidigger.minicraft.protocol.client.ClientLoginSuccess;
import me.minidigger.minicraft.protocol.client.ClientPlayChunkData;
import me.minidigger.minicraft.protocol.client.ClientPlayJoinGame;
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

public class MiniServerPacketHandler extends ServerPacketHandler {

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
    public PacketDirection getDirection() {
        return PacketDirection.TO_SERVER;
    }

    @Override
    public void handle(MiniConnection connection, ServerHandshake packet) {
        connection.setState(packet.getNextState());
        log.debug("Advancing state to {}", packet.getNextState());
    }

    @Override
    public void handle(MiniConnection connection, ServerStatusRequest packet) {
        ServerStatusResponse response = new ServerStatusResponse(
                new ServerStatusResponse.Version("HeyJA", 498),
                new ServerStatusResponse.Players(255, 0, List.of(
                        new ServerStatusResponse.Players.Player("Test", UUID.randomUUID()))),
                TextComponent.builder("Test.").color(TextColor.RED).append(" Test2").color(TextColor.GREEN).build(),
                server.getServerIcon());

        ClientStatusResponse responsePacket = new ClientStatusResponse();
        responsePacket.setResponse(response);

        connection.sendPacket(responsePacket);
    }

    @Override
    public void handle(MiniConnection connection, ServerStatusPing packet) {
        ClientStatusPong response = new ClientStatusPong();
        response.setPayload(packet.getPayload());
        connection.sendPacket(response);
    }

    @Override
    public void handle(MiniConnection connection, ServerLoginStart packet) {
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
    public void handle(MiniConnection connection, ServerPlayPluginMessage packet) {
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

    @Override
    public void handle(MiniConnection connection, ServerPlayKeepAlive serverPlayKeepAlive) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayClientSettings serverPlayClientSettings) {
        log.info("Language of {} is {}", connection.getPlayer().getName(), serverPlayClientSettings.getLocale());
    }

    @Override
    public void handle(MiniConnection connection, ServerPlayTeleportConfirm serverPlayTeleportConfirm) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPositionAndLook serverPlayPositionAndLook) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPosition serverPlayPosition) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayLook serverPlayLook) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPlayerAbilities serverPlayPlayerAbilities) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayBlockPlace serverPlayBlockPlace) {
        log.info("{} placed block at {}", connection.getPlayer().getName(), serverPlayBlockPlace.getPosition());
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

        ClientPlayChunkData chunkData = new ClientPlayChunkData();
        chunkData.setChunkPosition(new ChunkPosition(0, 0));
        chunkData.setFullChunk(true);

        ChunkData chunk = new ChunkData();
        chunkData.setChunkData(chunk);
        chunkData.setBlockEntities(new ListTag());

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                chunkData.setChunkPosition(new ChunkPosition(x, z));
                connection.sendPacket(chunkData);
            }
        }

        ClientPlayPositionAndLook positionAndLook = new ClientPlayPositionAndLook();
        positionAndLook.setPosition(new Position(0, 0, 0));
        connection.sendPacket(positionAndLook);

        connection.getPlayer().setConnection(connection);
        server.getPlayers().add(connection.getPlayer());
    }
}