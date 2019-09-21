package me.minidigger.miniserver.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.minidigger.miniserver.test.protocol.Packet;

public class MiniServerHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger log = LoggerFactory.getLogger(MiniServerHandler.class);

    private MiniConnection connection;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[+] Channel connected: {}", ctx.channel().remoteAddress());

        this.connection = new MiniConnection(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[-} Channel disconnected: {}", ctx.channel().remoteAddress());

        this.connection = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught, closing channel.", cause);

        this.connection = null;

        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        log.info("[Packet] {}", msg);
    }

    public MiniConnection getConnection() {
        return this.connection;
    }
}
