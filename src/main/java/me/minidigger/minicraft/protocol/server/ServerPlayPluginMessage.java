package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.Key;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ServerPlayPluginMessage extends MiniPacket {

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
