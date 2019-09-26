package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import net.kyori.text.Component;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.kyori.text.serializer.plain.PlainComponentSerializer;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.Packet;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.protocol.DataTypes;

public class ClientLoginDisconnect extends Packet {

    private Component reason;

    public Component getReason() {
        return reason;
    }

    public void setReason(Component reason) {
        this.reason = reason;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeString(GsonComponentSerializer.INSTANCE.serialize(reason), buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reason", PlainComponentSerializer.INSTANCE.serialize(reason))
                .toString();
    }
}
