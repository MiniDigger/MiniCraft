package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.Packet;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.protocol.DataTypes;

public class ServerLoginStart extends Packet {

    private String username;

    public String getUsername() {
        return username;
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
        this.username = DataTypes.readString(buf);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .toString();
    }
}
