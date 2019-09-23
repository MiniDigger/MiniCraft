package me.minidigger.miniserver.test.netty.pipeline;

import java.util.function.Consumer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import me.minidigger.miniserver.test.netty.MiniChannelHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketRegistry;

public class MiniPipeline extends ChannelInitializer<SocketChannel> {

    private final PacketRegistry packetRegistry;
    private final PacketHandler packetHandler;
    private final Consumer<MiniConnection> connectCallback;

    public MiniPipeline(PacketRegistry packetRegistry, PacketHandler packetHandler, Consumer<MiniConnection> connectCallback) {
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
        this.connectCallback = connectCallback;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("lengthDecoder", new MiniPacketLengthDecoder());
        pipeline.addLast("decoder", new MiniPacketDecoder(packetRegistry, packetHandler));

        pipeline.addLast("lengthEncoder", new MiniPacketLengthEncoder());
        pipeline.addLast("encoder", new MiniPacketEncoder(packetRegistry));

        pipeline.addLast("handler", new MiniChannelHandler(connectCallback));
    }
}
