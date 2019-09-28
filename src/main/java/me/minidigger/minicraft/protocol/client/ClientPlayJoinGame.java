package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.Dimension;
import me.minidigger.minicraft.model.GameMode;
import me.minidigger.minicraft.model.LevelType;
import me.minidigger.minicraft.protocol.MiniPacket;
import me.minidigger.minicraft.protocol.DataTypes;

public class ClientPlayJoinGame extends MiniPacket {

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
    public void toWire(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(gameMode.getId());
        buf.writeInt(dimension.getId());
        buf.writeByte(maxplayers);
        DataTypes.writeString(levelType.getId(), buf);
        DataTypes.writeVarInt(viewDistance, buf);
        buf.writeBoolean(reducedDebugInfo);
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
