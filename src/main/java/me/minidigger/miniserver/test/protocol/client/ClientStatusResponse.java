package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.ServerStatusResponse;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ClientStatusResponse extends Packet {

    private ServerStatusResponse response;

    public ServerStatusResponse getResponse() {
        return response;
    }

    public void setResponse(ServerStatusResponse response) {
        this.response = response;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
handler.handle(connection,this);
    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeJSON(response, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.response = DataTypes.readJSON(buf, ServerStatusResponse.class);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("response", response)
                .toString();
    }
}
