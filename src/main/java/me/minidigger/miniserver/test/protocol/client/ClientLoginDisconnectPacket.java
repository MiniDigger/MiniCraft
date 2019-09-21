package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import net.kyori.text.Component;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.text.serializer.plain.PlainComponentSerializer;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.server.MiniConnection;

public class ClientLoginDisconnectPacket extends Packet {

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
