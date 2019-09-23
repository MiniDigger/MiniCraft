package me.minidigger.miniserver.test.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.minidigger.miniserver.test.api.Client;
import me.minidigger.miniserver.test.console.MiniConsole;
import me.minidigger.miniserver.test.netty.MiniChannelHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;
import me.minidigger.miniserver.test.netty.pipeline.MiniPipeline;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketRegistry;

public class MiniClient {

    private final int port;
    private final String host;

    public MiniClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        new MiniClient("localhost", 25565).run();
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
