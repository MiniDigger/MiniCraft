package me.minidigger.minicraft.server;

import java.security.NoSuchAlgorithmException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.api.Server;
import me.minidigger.minicraft.console.MiniConsole;
import me.minidigger.minicraft.netty.pipeline.MiniPipeline;
import me.minidigger.minicraft.protocol.PacketRegistry;

public class MiniCraftServer {

    private final int port;

    public MiniCraftServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new MiniCraftServer(25565).run();
    }

    public void run() throws NoSuchAlgorithmException {
        PacketRegistry packetRegistry = new PacketRegistry();
        packetRegistry.init();

        Server server = new Server();
        server.start();
        MiniConsole serverConsole = new MiniConsole();
        serverConsole.start();

        PacketHandler packetHandler = new MiniServerPacketHandler(server);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MiniPipeline(packetRegistry, packetHandler, null));

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
