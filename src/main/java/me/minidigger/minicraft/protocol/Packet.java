package me.minidigger.minicraft.protocol;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.handler.PacketHandler;

public abstract class Packet {

    private PacketDirection direction;
    private PacketState state;
    private int id;

    public Packet() {
    }

    public PacketDirection getDirection() {
        return direction;
    }

    public PacketState getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public void setDirection(PacketDirection direction) {
        this.direction = direction;
    }

    public void setState(PacketState state) {
        this.state = state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void handle(MiniConnection connection, PacketHandler handler);

    public abstract void toWire(ByteBuf buf);

    public abstract void fromWire(ByteBuf buf);

    public abstract String toString();
}