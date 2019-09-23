package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ClientStatusPong extends Packet {

    private long payload;

    public long getPayload() {
        return payload;
    }

    public void setPayload(long payload) {
        this.payload = payload;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {
        buf.writeLong(payload);
    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.payload = buf.readLong();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("payload", payload)
                .toString();
    }
}
