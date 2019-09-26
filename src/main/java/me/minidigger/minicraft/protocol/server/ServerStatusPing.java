package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.Packet;
import me.minidigger.minicraft.protocol.handler.PacketHandler;

public class ServerStatusPing extends Packet {

    private long payload;

    public long getPayload() {
        return payload;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {
        buf.writeLong(this.payload);
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
