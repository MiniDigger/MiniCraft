package me.minidigger.miniserver.test.server;

import java.security.NoSuchAlgorithmException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.minidigger.miniserver.test.api.Server;
import me.minidigger.miniserver.test.console.MiniConsole;
import me.minidigger.miniserver.test.netty.pipeline.MiniPipeline;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketRegistry;

public class MiniServer {

    private final int port;

    public MiniServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new MiniServer(25565).run();
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
