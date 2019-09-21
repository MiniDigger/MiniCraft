package me.minidigger.miniserver.test.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketRegistry;
import me.minidigger.miniserver.test.pipeline.MiniPacketDecoder;
import me.minidigger.miniserver.test.pipeline.MiniPacketEncoder;
import me.minidigger.miniserver.test.pipeline.MiniPacketLengthDecoder;
import me.minidigger.miniserver.test.pipeline.MiniPacketLengthEncoder;

public class MiniServerInitializer extends ChannelInitializer<SocketChannel> {

    private final PacketRegistry packetRegistry;
    private final PacketHandler packetHandler;

    public MiniServerInitializer(PacketRegistry packetRegistry, PacketHandler packetHandler) {
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("lengthDecoder", new MiniPacketLengthDecoder());
        pipeline.addLast("decoder", new MiniPacketDecoder(packetRegistry, packetHandler));

        pipeline.addLast("lengthEncoder", new MiniPacketLengthEncoder());
        pipeline.addLast("encoder", new MiniPacketEncoder());

        pipeline.addLast("handler", new MiniServerHandler());
    }
}
