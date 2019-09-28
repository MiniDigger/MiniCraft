package me.minidigger.minicraft.protocol.server;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ServerPlayPlayerAbilities extends MiniPacket {

    private byte flags;
    private float flyingSpeed;
    private float walkingSpeed;

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
