package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.Key;
import me.minidigger.minicraft.protocol.MiniPacket;
import me.minidigger.minicraft.protocol.DataTypes;

public class ClientPlayPluginMessage extends MiniPacket {

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
        DataTypes.writeString(channel.combined(), buf);
        DataTypes.writeByteArray(data, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel)
                .add("data", data)
                .toString();
    }
}
