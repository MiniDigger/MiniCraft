package me.minidigger.minicraft.server;

import java.security.NoSuchAlgorithmException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.minidigger.minicraft.App;
import me.minidigger.minicraft.protocol.MiniPacketHandler;
import me.minidigger.minicraft.api.Server;
import me.minidigger.minicraft.netty.pipeline.MiniPipeline;

public class MiniCraftServer extends App {

    private final int port;

    private MiniPacketHandler packetHandler;

    public MiniCraftServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new MiniCraftServer(25565).run();
    }

    public void run() throws NoSuchAlgorithmException {
        init();

        Server server = new Server();
        server.start();

        packetHandler = new MiniServerPacketHandler(server, this);
        packetHandler.init();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MiniPipeline(getPacketRegistry(), getPacketHandler(), null));

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public String getAppName() {
        return "MiniCraftServer";
    }

    @Override
    public MiniPacketHandler getPacketHandler() {
        return packetHandler;
    }
}
