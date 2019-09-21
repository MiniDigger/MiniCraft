package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.Position;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.server.MiniConnection;

public class ClientPlayPositionAndLook extends Packet {

    private Position position;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("position", position)
                .toString();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writePosition(position, buf);
        buf.writeByte(0x0); // relative flag, ignored for now
        DataTypes.writeVarInt(-1, buf); // teleport id, ignored for now
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

}
