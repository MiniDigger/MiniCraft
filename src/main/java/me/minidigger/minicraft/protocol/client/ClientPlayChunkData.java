package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;

import net.kyori.nbt.ListTag;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.Packet;
import me.minidigger.minicraft.protocol.handler.PacketHandler;
import me.minidigger.minicraft.model.chunk.ChunkData;
import me.minidigger.minicraft.model.chunk.ChunkPosition;
import me.minidigger.minicraft.protocol.DataTypes;

public class ClientPlayChunkData extends Packet {

    private ChunkPosition chunkPosition;
    private boolean fullChunk = true;
    private ChunkData chunkData;
    private ListTag blockEntities;

    public ChunkPosition getChunkPosition() {
        return chunkPosition;
    }

    public void setChunkPosition(ChunkPosition chunkPosition) {
        this.chunkPosition = chunkPosition;
    }

    public boolean isFullChunk() {
        return fullChunk;
    }

    public void setFullChunk(boolean fullChunk) {
        this.fullChunk = fullChunk;
    }

    public ListTag getBlockEntities() {
        return blockEntities;
    }

    public void setBlockEntities(ListTag blockEntities) {
        this.blockEntities = blockEntities;
    }

    public ChunkData getChunkData() {
        return chunkData;
    }

    public void setChunkData(ChunkData chunkData) {
        this.chunkData = chunkData;
    }

    @Override
    public void handle(MiniConnection connection, PacketHandler handler) {

    }

    @Override
    public void toWire(ByteBuf buf) {
        buf.writeInt(chunkPosition.getX());
        buf.writeInt(chunkPosition.getZ());
        buf.writeBoolean(fullChunk);
        try {
            chunkData.toWire(buf);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        DataTypes.writeNBTList(blockEntities, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("chunkPosition", chunkPosition)
                .toString();
    }
}
