package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerStatusRequest extends Packet {
    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        // no data
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
