package me.minidigger.miniserver.test.server;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.minidigger.miniserver.test.protocol.PacketHandler;
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

        PacketHandler packetHandler = new MiniServerPacketHandler();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MiniServerInitializer(packetRegistry, packetHandler));

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
