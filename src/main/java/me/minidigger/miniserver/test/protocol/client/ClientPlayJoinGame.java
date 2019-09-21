package me.minidigger.miniserver.test.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.Dimension;
import me.minidigger.miniserver.test.model.GameMode;
import me.minidigger.miniserver.test.model.LevelType;
import me.minidigger.miniserver.test.protocol.DataTypes;
import me.minidigger.miniserver.test.protocol.Packet;
import me.minidigger.miniserver.test.protocol.PacketHandler;
import me.minidigger.miniserver.test.server.MiniConnection;

public class ClientPlayJoinGame extends Packet {

    private int entityId;
    private GameMode gameMode;
    private Dimension dimension;
    private int maxplayers;
    private LevelType levelType;
    private int viewDistance;
    private boolean reducedDebugInfo;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public int getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(int maxplayers) {
        this.maxplayers = maxplayers;
    }

    public LevelType getLevelType() {
        return levelType;
    }

    public void setLevelType(LevelType levelType) {
        this.levelType = levelType;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public boolean isReducedDebugInfo() {
        return reducedDebugInfo;
    }

    public void setReducedDebugInfo(boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(gameMode.getId());
        buf.writeInt(dimension.getId());
        buf.writeByte(maxplayers);
        DataTypes.writeString(levelType.getId(), buf);
        DataTypes.writeVarInt(viewDistance, buf);
        DataTypes.writeBoolean(reducedDebugInfo, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entityId", entityId)
                .add("gameMode", gameMode)
                .add("dimension", dimension)
                .add("maxplayers", maxplayers)
                .add("levelType", levelType)
                .add("viewDistance", viewDistance)
                .add("reducedDebugInfo", reducedDebugInfo)
                .toString();
    }
}
