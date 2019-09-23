package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.BlockFace;
import me.minidigger.miniserver.test.model.BlockPosition;
import me.minidigger.miniserver.test.model.Hand;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerPlayBlockPlace extends Packet {

    private Hand hand;
    private BlockPosition position;
    private BlockFace face;
    private float cursorX;
    private float cursorY;
    private float cursorZ;
    private boolean insideBlock;

    public Hand getHand() {
        return hand;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public BlockFace getFace() {
        return face;
    }

    public float getCursorX() {
        return cursorX;
    }

    public float getCursorY() {
        return cursorY;
    }

    public float getCursorZ() {
        return cursorZ;
    }

    public boolean isInsideBlock() {
        return insideBlock;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    @Override
    public void toWire(ByteBuf buf) {

    }

    @Override
    public void fromWire(ByteBuf buf) {
        this.hand = Hand.fromId(DataTypes.readVarInt(buf));
        this.position = DataTypes.readBlockPosition(buf);
        this.face = BlockFace.fromId(DataTypes.readVarInt(buf));
        this.cursorX = buf.readFloat();
        this.cursorY = buf.readFloat();
        this.cursorZ = buf.readFloat();
        this.insideBlock = buf.readBoolean();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("hand", hand)
                .add("position", position)
                .add("face", face)
                .add("cursorX", cursorX)
                .add("cursorY", cursorY)
                .add("cursorZ", cursorZ)
                .add("insideBlock", insideBlock)
                .toString();
    }
}
