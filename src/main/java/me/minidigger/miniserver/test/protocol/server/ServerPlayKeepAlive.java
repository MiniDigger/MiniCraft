package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerPlayKeepAlive extends Packet {

    private long id;

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.id = buf.readLong();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
