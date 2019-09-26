package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.Packet;

public class ServerLoginEncryptionResponse extends Packet {

    private byte[] sharedSecret;
    private byte[] token;

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

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
