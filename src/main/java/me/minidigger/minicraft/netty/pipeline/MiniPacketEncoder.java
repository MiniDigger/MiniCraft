package me.minidigger.minicraft.netty.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.minidigger.minicraft.protocol.MiniPacketRegistry;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class MiniPacketEncoder extends MessageToByteEncoder<MiniPacket> {

    private static final Logger log = LoggerFactory.getLogger(MiniPacketEncoder.class);

    private final MiniPacketRegistry packetRegistry;

    public MiniPacketEncoder(MiniPacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MiniPacket packet, ByteBuf out) throws Exception {
        packetRegistry.fillInfo(packet);
        log.debug("Writing packet {}/{}: {}", packet.getState(), packet.getId(), packet);

        DataTypes.writeVarInt(packet.getId(), out);
        packet.toWire(out);
    }
}
