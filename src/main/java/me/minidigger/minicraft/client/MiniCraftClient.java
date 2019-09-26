package me.minidigger.minicraft.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.minidigger.minicraft.api.Client;
import me.minidigger.minicraft.console.MiniConsole;
import me.minidigger.minicraft.netty.pipeline.MiniPipeline;
import me.minidigger.minicraft.protocol.PacketRegistry;
import me.minidigger.minicraft.protocol.handler.PacketHandler;

public class MiniCraftClient {

    private final int port;
    private final String host;

    public MiniCraftClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        new MiniCraftClient("localhost", 25565).run();
    }

    public void run() {
        PacketRegistry packetRegistry = new PacketRegistry();
        packetRegistry.init();

        Client client = new Client("MiniDigger");
        client.start();
        MiniConsole serverConsole = new MiniConsole();
        serverConsole.start();

        PacketHandler packetHandler = new MiniClientPacketHandler(client);

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new MiniPipeline(packetRegistry, packetHandler, client::setConnection));

            Channel channel = bootstrap.connect(host, port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
