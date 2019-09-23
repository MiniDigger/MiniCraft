package me.minidigger.miniserver.test.netty.pipeline;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.minidigger.miniserver.test.protocol.DataTypes;

public class MiniPacketLengthDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        int packetLength = DataTypes.readVarInt(in);

        if (in.readableBytes() < packetLength) {
            in.resetReaderIndex();
        } else {
            out.add(in.readBytes(packetLength));
        }
    }
}
