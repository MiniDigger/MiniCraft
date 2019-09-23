package me.minidigger.miniserver.test.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.minidigger.miniserver.test.protocol.Packet;

public class MiniChannelHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger log = LoggerFactory.getLogger(MiniChannelHandler.class);

    private MiniConnection connection;
    private Consumer<MiniConnection> connectCallback;

    public MiniChannelHandler(Consumer<MiniConnection> connectCallback) {
        this.connectCallback = connectCallback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[+] Channel connected: {}", ctx.channel().remoteAddress());

        this.connection = new MiniConnection(ctx);
        if (this.connectCallback != null) {
            this.connectCallback.accept(this.connection);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[-] Channel disconnected: {}", ctx.channel().remoteAddress());

        this.connection = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if ("Connection reset".equals(cause.getMessage())) {
            log.error("{}: Connection reset.", this.connection.getRemoteAddress());
        } else {
            log.error("{}: Exception caught, closing channel.", this.connection.getRemoteAddress(), cause);
        }

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
