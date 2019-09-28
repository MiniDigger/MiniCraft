package me.minidigger.minicraft.protocol.server;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.ChatMode;
import me.minidigger.minicraft.model.Hand;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ServerPlayClientSettings extends MiniPacket {

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
