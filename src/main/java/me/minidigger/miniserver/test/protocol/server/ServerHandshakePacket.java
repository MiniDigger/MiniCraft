package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.protocol.PacketState;
import me.minidigger.miniserver.test.server.MiniConnection;

public class ServerHandshakePacket extends Packet {

    private int protocolVersion;
    private String serverAddress;
    private short serverPort;
    private PacketState nextState;

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public short getServerPort() {
        return serverPort;
    }

    public PacketState getNextState() {
        return nextState;
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.protocolVersion = DataTypes.readVarInt(buf);
        this.serverAddress = DataTypes.readString(buf);
        this.serverPort = DataTypes.readShort(buf);
        int nextState = DataTypes.readVarInt(buf);
        if (nextState == 1) {
            this.nextState = PacketState.STATUS;
        } else if (nextState == 2) {
            this.nextState = PacketState.LOGIN;
        }
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("protocolVersion", protocolVersion)
                .add("serverAddress", serverAddress)
                .add("serverPort", serverPort)
                .add("nextState", nextState)
                .toString();
    }
}
