package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.Key;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerPlayPluginMessage extends Packet {

    private Key channel;
    private byte[] data;

    public Key getChannel() {
        return channel;
    }

    public void setChannel(Key channel) {
        this.channel = channel;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        String combined = DataTypes.readString(buf);
        String[] split = combined.split(":");
        if(split.length >=2) {
            channel = Key.of(split[0], combined.replace(split[0] + ":",""));
            data = DataTypes.readByteArray(buf);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel)
                .add("data", data)
                .toString();
    }
}
