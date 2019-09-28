package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;
import me.minidigger.minicraft.protocol.PacketState;

public class ServerHandshake extends MiniPacket {

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

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(short serverPort) {
        this.serverPort = serverPort;
    }

    public void setNextState(PacketState nextState) {
        this.nextState = nextState;
    }

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeVarInt(this.protocolVersion, buf);
        DataTypes.writeString(this.serverAddress, buf);
        DataTypes.writeShort(this.serverPort, buf);
        DataTypes.writeVarInt(this.getNextState().getId(), buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.protocolVersion = DataTypes.readVarInt(buf);
        this.serverAddress = DataTypes.readString(buf);
        this.serverPort = DataTypes.readShort(buf);
        this.nextState = PacketState.fromId(DataTypes.readVarInt(buf));
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
