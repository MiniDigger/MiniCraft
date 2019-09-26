package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.Packet;

public class ServerPlayTeleportConfirm extends Packet {

    private int teleportId;

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection,this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("teleportId", teleportId)
                .toString();
    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.teleportId = DataTypes.readVarInt(buf);
    }

}
