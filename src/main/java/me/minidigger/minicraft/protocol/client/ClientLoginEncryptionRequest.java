package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ClientLoginEncryptionRequest extends MiniPacket {

    private String serverId;
    private byte[] key;
    private byte[] token;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeString(serverId, buf);
        DataTypes.writeByteArray(key, buf);
        DataTypes.writeByteArray(token, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("serverId", serverId)
                .add("key", key)
                .add("token", token)
                .toString();
    }
}
