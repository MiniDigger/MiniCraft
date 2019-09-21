package me.minidigger.miniserver.test.server.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.minidigger.miniserver.test.protocol.DataTypes;

public class MiniPacketLengthEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        DataTypes.writeVarInt(msg.readableBytes(), out);
        out.writeBytes(msg);
    }
}
