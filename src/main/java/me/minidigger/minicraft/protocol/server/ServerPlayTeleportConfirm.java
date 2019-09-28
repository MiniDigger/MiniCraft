package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ServerPlayTeleportConfirm extends MiniPacket {

    private int teleportId;

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
