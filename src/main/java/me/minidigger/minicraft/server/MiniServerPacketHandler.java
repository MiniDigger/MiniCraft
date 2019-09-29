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

import me.minidigger.minicraft.api.Player;
import me.minidigger.minicraft.api.Server;
import me.minidigger.minicraft.model.Dimension;
import me.minidigger.minicraft.model.GameMode;
import me.minidigger.minicraft.model.Key;
import me.minidigger.minicraft.model.LevelType;
import me.minidigger.minicraft.model.Position;
import me.minidigger.minicraft.model.ServerStatusResponse;
import me.minidigger.minicraft.model.chunk.ChunkData;
import me.minidigger.minicraft.model.chunk.ChunkPosition;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.MiniPacketHandler;
import me.minidigger.minicraft.protocol.PacketDirection;
import me.minidigger.minicraft.protocol.PacketState;
import me.minidigger.minicraft.protocol.client.ClientLoginEncryptionRequest;
import me.minidigger.minicraft.protocol.client.ClientLoginSuccess;
import me.minidigger.minicraft.protocol.client.ClientPlayChunkData;
import me.minidigger.minicraft.protocol.client.ClientPlayDeclareCommands;
import me.minidigger.minicraft.protocol.client.ClientPlayJoinGame;
import me.minidigger.minicraft.protocol.client.ClientPlayPluginMessage;
import me.minidigger.minicraft.protocol.client.ClientPlayPositionAndLook;
import me.minidigger.minicraft.protocol.client.ClientStatusPong;
import me.minidigger.minicraft.protocol.client.ClientStatusResponse;
import me.minidigger.minicraft.protocol.server.ServerHandshake;
import me.minidigger.minicraft.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.minicraft.protocol.server.ServerLoginStart;
import me.minidigger.minicraft.protocol.server.ServerPlayBlockPlace;
import me.minidigger.minicraft.protocol.server.ServerPlayChatMessage;
import me.minidigger.minicraft.protocol.server.ServerPlayClientSettings;
import me.minidigger.minicraft.protocol.server.ServerPlayPluginMessage;
import me.minidigger.minicraft.protocol.server.ServerStatusPing;
import me.minidigger.minicraft.protocol.server.ServerStatusRequest;

public class MiniServerPacketHandler extends MiniPacketHandler {

    private static final Logger log = LoggerFactory.getLogger(MiniServerPacketHandler.class);

    private KeyPair pair;

    private Server server;
    private MiniCraftServer app;

    public MiniServerPacketHandler(Server server, MiniCraftServer miniCraftServer) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        pair = keyGen.generateKeyPair();

        this.server = server;
        this.app = miniCraftServer;
    }

    @Override
    public PacketDirection getDirection() {
        return PacketDirection.TO_SERVER;
    }

    @Override
    public void init() {
        registerCallback(ServerHandshake.class, (connection, packet) -> {
            connection.setState(packet.getNextState());
            log.debug("Advancing state to {}", packet.getNextState());
        });
        registerCallback(ServerStatusRequest.class, (connection, packet) -> {
            ServerStatusResponse response = new ServerStatusResponse(
                    new ServerStatusResponse.Version("HeyJA", 498),
                    new ServerStatusResponse.Players(255, 0, List.of(
                            new ServerStatusResponse.Players.Player("Test", UUID.randomUUID()))),
                    TextComponent.builder("Test.").color(TextColor.RED).append(" Test2").color(TextColor.GREEN).build(),
                    server.getServerIcon());

            ClientStatusResponse responsePacket = new ClientStatusResponse();
            responsePacket.setResponse(response);

            connection.sendPacket(responsePacket);
        });
        registerCallback(ServerStatusPing.class, (connection, packet) -> {
            ClientStatusPong response = new ClientStatusPong();
            response.setPayload(packet.getPayload());
            connection.sendPacket(response);
        });
        registerCallback(ServerLoginStart.class, (connection, packet) -> {
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
        });
        registerCallback(ServerLoginEncryptionResponse.class, (connection, packet) -> {
            join(connection);
        });
        registerCallback(ServerPlayPluginMessage.class, (connection, packet) -> {
            Player player = connection.getPlayer();
            player.setBrand(new String(packet.getData(), StandardCharsets.UTF_8));
            log.info("Client brand of {} is {}", player.getName(), player.getBrand());
        });
        registerCallback(ServerPlayChatMessage.class, (connection, packet) -> {
            if(packet.getMessage().startsWith("/")) {
                app.getCommandManager().dispatchCommand(connection.getPlayer(),packet.getMessage());
                return;
            }
            log.info("[CHAT] <{}> {}", connection.getPlayer().getName(), packet.getMessage());

            Component msg = TextComponent.builder("<" + connection.getPlayer().getName() + "> ").color(TextColor.WHITE)
                    .append(packet.getMessage()).color(TextColor.GRAY).build();

            for (Player player : server.getPlayers()) {
                player.sendMessage(msg);
            }
        });
        registerCallback(ServerPlayClientSettings.class, (connection, packet) -> {
            log.info("Language of {} is {}", connection.getPlayer().getName(), packet.getLocale());
        });
        registerCallback(ServerPlayBlockPlace.class, (connection, packet) -> {
            log.info("{} placed block at {}", connection.getPlayer().getName(), packet.getPosition());
        });
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

        ClientPlayDeclareCommands declareCommands = new ClientPlayDeclareCommands();
        declareCommands.setRootCommandNode(app.getCommandManager().getBrigadierManager().getRoot());
        connection.sendPacket(declareCommands);

        connection.getPlayer().setConnection(connection);
        server.getPlayers().add(connection.getPlayer());
    }
}
