package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import net.kyori.text.Component;
import net.kyori.text.serializer.gson.GsonComponentSerializer;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.ChatPosition;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.Packet;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.protocol.DataTypes;

public class ClientPlayChatMessage extends Packet {

    private Component component;
    private ChatPosition position;

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public ChatPosition getPosition() {
        return position;
    }

    public void setPosition(ChatPosition position) {
        this.position = position;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeString(GsonComponentSerializer.INSTANCE.serialize(component),buf);
        buf.writeByte(position.getId());
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("component", component)
                .add("position", position)
                .toString();
    }
}
