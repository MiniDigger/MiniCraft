package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.model.Position;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.Packet;

public class ServerPlayPosition extends Packet {

    private Position position;
    private boolean onGround;

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection,this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.position = DataTypes.readPosition(false, buf);
        this.onGround = buf.readBoolean();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("position", position)
                .add("onGround", onGround)
                .toString();
    }
}
