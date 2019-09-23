package me.minidigger.miniserver.test.protocol.server;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerPlayPlayerAbilities extends Packet {

    private byte flags;
    private float flyingSpeed;
    private float walkingSpeed;

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.flags = buf.readByte();
        this.flyingSpeed = buf.readFloat();
        this.walkingSpeed = buf.readFloat();
    }

    @Override
    public String toString() {
        return null;
    }
}
