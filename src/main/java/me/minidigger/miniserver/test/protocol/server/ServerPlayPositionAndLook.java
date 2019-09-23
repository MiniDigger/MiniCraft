package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.Position;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerPlayPositionAndLook extends Packet {

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
        this.position = DataTypes.readPosition(true, buf);
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
