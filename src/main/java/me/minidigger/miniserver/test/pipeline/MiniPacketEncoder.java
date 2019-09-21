package me.minidigger.miniserver.test.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketRegistry;

public class MiniPacketEncoder extends MessageToByteEncoder<Packet> {

    private static final Logger log = LoggerFactory.getLogger(MiniPacketEncoder.class);

    private final PacketRegistry packetRegistry;

    public MiniPacketEncoder(PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        packetRegistry.fillInfo(packet);
        log.info("Writing packet {}/{}: {}", packet.getState(), packet.getId(), packet);

        DataTypes.writeVarInt(packet.getId(), out);
        packet.toWire(out);
    }
}
