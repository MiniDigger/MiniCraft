package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.Position;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.server.MiniConnection;

public class ServerPlayLook extends Packet {

    private float pitch, yaw;
    private boolean onGround;

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
                .add("pitch", pitch)
                .add("yaw", yaw)
                .add("onGround", onGround)
                .toString();
    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.pitch = buf.readFloat();
        this.yaw = buf.readFloat();
        this.onGround = buf.readBoolean();
    }

}
