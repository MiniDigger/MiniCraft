package me.minidigger.miniserver.test.netty.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.minidigger.miniserver.test.protocol.DataTypes;

public class MiniPacketLengthEncoder extends MessageToByteEncoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(MiniPacketLengthEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        DataTypes.writeVarInt(msg.readableBytes(), out);
        log.debug("Wrote packet with size {}", msg.readableBytes());
        out.writeBytes(msg);
    }
}
