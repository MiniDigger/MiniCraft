package me.minidigger.minicraft.netty.pipeline;

import java.util.function.Consumer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import me.minidigger.minicraft.netty.MiniChannelHandler;
import me.minidigger.minicraft.protocol.MiniPacketHandler;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.MiniPacketRegistry;

public class MiniPipeline extends ChannelInitializer<SocketChannel> {

    private final MiniPacketRegistry packetRegistry;
    private final MiniPacketHandler packetHandler;
    private final Consumer<MiniConnection> connectCallback;

    public MiniPipeline(MiniPacketRegistry packetRegistry, MiniPacketHandler packetHandler, Consumer<MiniConnection> connectCallback) {
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
