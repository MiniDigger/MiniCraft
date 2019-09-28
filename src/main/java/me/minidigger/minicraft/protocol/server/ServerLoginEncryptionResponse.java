package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ServerLoginEncryptionResponse extends MiniPacket {

    private byte[] sharedSecret;
    private byte[] token;

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        sharedSecret = DataTypes.readByteArray(buf);
        token = DataTypes.readByteArray(buf);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sharedSecret", sharedSecret)
                .add("token", token)
                .toString();
    }
}
