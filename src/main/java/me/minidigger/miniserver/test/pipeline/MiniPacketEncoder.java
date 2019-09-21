package me.minidigger.miniserver.test.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;

public class MiniPacketEncoder extends MessageToByteEncoder<Packet> {

    private static final Logger log = LoggerFactory.getLogger(MiniPacketEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        log.info("Writing packet {}", packet);
        DataTypes.writeVarInt(packet.getId(), out);
        packet.toWire(out);
    }
}
