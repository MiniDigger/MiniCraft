package me.minidigger.miniserver.test.model.chunk;

import net.kyori.nbt.CompoundTag;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.minidigger.miniserver.test.GameConstants;
import me.minidigger.miniserver.test.protocol.DataTypes;

public class ChunkData {

    private ChunkSection[] sections = new ChunkSection[GameConstants.CHUNK_HEIGHT / GameConstants.SECTION_HEIGHT];
    private byte[] heightMap = new byte[GameConstants.CHUNK_HEIGHT * GameConstants.SECTION_HEIGHT];
    ;
    private int[] biomes = new int[256];

    public ChunkData() {
        // set dummy data
        Arrays.fill(biomes, 0); // 127 = void, 0 = ocean
        Arrays.fill(sections, new ChunkSection());

//        for(ChunkSection section : sections) {
            for (int i = 0; i < 16; i++) {
                sections[0].set(index(8, i, 8), i);
            }

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    sections[0].set(index(j, 0, i), 4088);
                }
            }
//        }
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public void toWire(ByteBuf buf) {
        int mask = 0;
        ByteBuf buffer = Unpooled.buffer();
        for (int sectionY = 0; sectionY < (GameConstants.CHUNK_HEIGHT / GameConstants.SECTION_HEIGHT); sectionY++) {
            if (!sections[sectionY].isEmpty()) {
                mask |= (1 << sectionY);
                sections[sectionY].toWire(buffer);
            }
        }
        for (int z = 0; z < GameConstants.SECTION_WIDTH; z++) {
            for (int x = 0; x < GameConstants.SECTION_WIDTH; x++) {
                buffer.writeInt(getBiome(x, z));
            }
        }

        DataTypes.writeVarInt(mask, buf);
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putLongArray("MOTION_BLOCKING", new long[256 * 9 / 64]);
        DataTypes.writeNBT(compoundTag, buf);

        DataTypes.writeVarInt(buffer.readableBytes(), buf);
        buf.writeBytes(buffer);
        buffer.release();

//        buf.writeByte(0);
//        DataTypes.writeIntArray(biomes, buf);
    }

    public void setBiome(int x, int z, int id) {
        biomes[z * 16 | x] = id;
    }

    public int getBiome(int x, int z) {
        return biomes[z * 16 | x];
    }

}
