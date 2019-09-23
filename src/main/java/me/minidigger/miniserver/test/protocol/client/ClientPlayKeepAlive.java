package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ClientPlayKeepAlive extends Packet {

    private long id = System.currentTimeMillis();

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        buf.writeLong(id);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
