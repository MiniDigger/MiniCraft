package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.MiniPacket;
import me.minidigger.minicraft.model.ServerStatusResponse;
import me.minidigger.minicraft.protocol.DataTypes;

public class ClientStatusResponse extends MiniPacket {

    private ServerStatusResponse response;

    public ServerStatusResponse getResponse() {
        return response;
    }

    public void setResponse(ServerStatusResponse response) {
        this.response = response;
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
