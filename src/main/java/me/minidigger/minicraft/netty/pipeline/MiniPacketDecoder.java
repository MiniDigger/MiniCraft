package me.minidigger.minicraft.netty.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.minidigger.minicraft.netty.MiniChannelHandler;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.MiniPacketRegistry;
import me.minidigger.minicraft.protocol.MiniPacketHandler;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class MiniPacketDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(MiniPacketDecoder.class);

    private MiniPacketRegistry packetRegistry;
    private MiniPacketHandler packetHandler;

    public MiniPacketDecoder(MiniPacketRegistry packetRegistry, MiniPacketHandler packetHandler) {
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MiniConnection connection = ctx.pipeline().get(MiniChannelHandler.class).getConnection();

        int packetId = DataTypes.readVarInt(in);
        log.debug("got packet id {}, state is {}, bytes to read {}", packetId, connection.getState(), in.readableBytes());
        Class<? extends MiniPacket> packetClass = packetRegistry.getPacket(packetHandler.getDirection(), connection.getState(), packetId);

        if (packetClass == null) {
            log.warn("Couldn't find a packet class for {}:{}:{}", packetHandler.getDirection(), connection.getState(), packetId);
            in.skipBytes(in.readableBytes());
            return;
        }

        MiniPacket packet = (MiniPacket) packetClass.getConstructors()[0].newInstance();
        packet.setDirection(packetHandler.getDirection());
        packet.setState(connection.getState());
        packet.setId(packetId);

        packet.fromWire(in);

        log.debug("packet is {}", packet);

        packetHandler.handle(connection, packet);

        if (in.readableBytes() > 0) {
            log.warn("Didn't fully read packet {}! {} bytes to go", packet.getClass().getSimpleName(), in.readableBytes());
            in.skipBytes(in.readableBytes());
        }
    }
}
