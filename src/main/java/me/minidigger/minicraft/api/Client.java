package me.minidigger.minicraft.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import javax.security.auth.callback.Callback;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.minidigger.minicraft.client.MiniCraftClient;
import me.minidigger.minicraft.model.ServerStatusResponse;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.netty.pipeline.MiniPipeline;
import me.minidigger.minicraft.protocol.PacketState;
import me.minidigger.minicraft.protocol.client.ClientStatusResponse;
import me.minidigger.minicraft.protocol.server.ServerHandshake;
import me.minidigger.minicraft.protocol.server.ServerPlayChatMessage;
import me.minidigger.minicraft.protocol.server.ServerStatusRequest;

public class Client {

    private String username;
    private MiniCraftClient miniCraftClient;

    public Client(String username, MiniCraftClient miniCraftClient) {
        this.username = username;
        this.miniCraftClient = miniCraftClient;
    }

    public void sendMessage(MiniConnection connection, String msg) {
        ServerPlayChatMessage chatMessage = new ServerPlayChatMessage();
        chatMessage.setMessage(msg);
        connection.sendPacket(chatMessage);
    }

    public void connect(String hostname, int port, Consumer<MiniConnection> callback) {
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();

            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new MiniPipeline(miniCraftClient.getPacketRegistry(), miniCraftClient.getPacketHandler(), callback));

                // wait till connection should be closed
                bootstrap.connect(hostname, port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }, "ConnectionThread " + hostname + ":" + port).start();
    }

    public CompletableFuture<ServerStatusResponse> doServerListPing(String hostname, int port) {
        CompletableFuture<ServerStatusResponse> future = new CompletableFuture<>();
        connect(hostname, port, (connection) -> {
            ServerHandshake handshake = new ServerHandshake();
            handshake.setProtocolVersion(-1);
            handshake.setServerAddress(hostname);
            handshake.setServerPort((short) port);
            handshake.setNextState(PacketState.STATUS);
            connection.sendPacket(handshake);
            connection.setState(PacketState.STATUS);

            miniCraftClient.getPacketHandler().registerCallback(ClientStatusResponse.class, (connection1, clientStatusResponse) -> {
                connection1.close();
                future.complete(clientStatusResponse.getResponse());
            });

            ServerStatusRequest statusRequest = new ServerStatusRequest();
            connection.sendPacket(statusRequest);
        });
        return future;
    }
}
