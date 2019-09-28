package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.Position;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ClientPlayPositionAndLook extends MiniPacket {

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
    public void toWire(ByteBuf buf) {
        DataTypes.writePosition(true, position, buf);
        buf.writeByte(0x0); // relative flag, ignored for now
        DataTypes.writeVarInt(-1, buf); // teleport id, ignored for now
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

}
