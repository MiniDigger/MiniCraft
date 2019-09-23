package me.minidigger.miniserver.test.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.ChatMode;
import me.minidigger.miniserver.test.model.Hand;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.handler.PacketHandler;
import me.minidigger.miniserver.test.netty.MiniConnection;

public class ServerPlayClientSettings extends Packet {

    private String locale;
    private byte viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private byte displayedSkinParts;
    private Hand mainHand;

    public String getLocale() {
        return locale;
    }

    public byte getViewDistance() {
        return viewDistance;
    }

    public ChatMode getChatMode() {
        return chatMode;
    }

    public boolean isChatColors() {
        return chatColors;
    }

    public byte getDisplayedSkinParts() {
        return displayedSkinParts;
    }

    public Hand getMainHand() {
        return mainHand;
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
        this.locale = DataTypes.readString(buf);
        this.viewDistance = buf.readByte();
        this.chatMode = ChatMode.fromId(DataTypes.readVarInt(buf));
        this.chatColors = buf.readBoolean();
        this.displayedSkinParts = buf.readByte();
        this.mainHand = Hand.fromId(DataTypes.readVarInt(buf));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("locale", locale)
                .add("viewDistance", viewDistance)
                .add("chatMode", chatMode)
                .add("chatColors", chatColors)
                .add("displayedSkinParts", displayedSkinParts)
                .add("mainHand", mainHand)
                .toString();
    }
}
