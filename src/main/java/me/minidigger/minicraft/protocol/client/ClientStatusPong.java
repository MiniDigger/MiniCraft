package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ClientStatusPong extends MiniPacket {

    private long payload;

    public long getPayload() {
        return payload;
    }

    public void setPayload(long payload) {
        this.payload = payload;
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
