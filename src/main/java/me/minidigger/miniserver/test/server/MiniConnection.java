package me.minidigger.miniserver.test.server;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketState;

public class MiniConnection {

    private final ChannelHandlerContext ctx;
    private PacketState state = PacketState.HANDSHAKE;
    private String username;

    public MiniConnection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public PacketState getState() {
        return state;
    }

    public void setState(PacketState state) {
        this.state = state;
    }

    public SocketAddress getRemoteAddress() {
        return ctx.channel().remoteAddress();
    }

    public void sendPacket(Packet packet) {
        ctx.channel().writeAndFlush(packet);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
