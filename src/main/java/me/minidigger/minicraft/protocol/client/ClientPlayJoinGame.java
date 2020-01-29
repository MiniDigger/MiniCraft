package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.Dimension;
import me.minidigger.minicraft.model.GameMode;
import me.minidigger.minicraft.model.LevelType;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ClientPlayJoinGame extends MiniPacket {

    private int entityId;
    private GameMode gameMode;
    private Dimension dimension;
    private int maxplayers;
    private LevelType levelType;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean respawnScreen;
    private long seedHash;

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

    public boolean isRespawnScreen() {
        return respawnScreen;
    }

    public void setRespawnScreen(boolean respawnScreen) {
        this.respawnScreen = respawnScreen;
    }

    public long getSeedHash() {
        return seedHash;
    }

    public void setSeedHash(long seedHash) {
        this.seedHash = seedHash;
    }

    @Override
    public void toWire(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(gameMode.getId());
        buf.writeInt(dimension.getId());
        buf.writeLong(seedHash);
        buf.writeByte(maxplayers);
        DataTypes.writeString(levelType.getId(), buf);
        DataTypes.writeVarInt(viewDistance, buf);
        buf.writeBoolean(reducedDebugInfo);
        buf.writeBoolean(respawnScreen);
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
