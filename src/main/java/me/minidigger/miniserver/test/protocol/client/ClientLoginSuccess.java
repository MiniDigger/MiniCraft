package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.server.MiniConnection;

public class ClientLoginSuccess extends Packet {

    private String username;
    private UUID uuid;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("uuid", uuid)
                .toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeString(uuid.toString(), buf);
        DataTypes.writeString(username, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

}
