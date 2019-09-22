package me.minidigger.miniserver.test.model.chunk;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.GameConstants;
import me.minidigger.miniserver.test.protocol.DataTypes;

public class ChunkSection {

    private short blockCount = 16 * 16 * 16;
    private int bitsPerBlock = 14; // direct, no palette, for now
    private int[] palette;
    private long[] data;

    private int size;
    private long maxEntryValue;

    public ChunkSection() {
        this.data = new long[(16 * 16 * 16) * bitsPerBlock / 64];

        this.size = this.data.length * 64 / this.bitsPerBlock;
        this.maxEntryValue = (1L << this.bitsPerBlock) - 1;
    }

    public void toWire(ByteBuf buf) {
        buf.writeShort(blockCount);
        buf.writeByte(bitsPerBlock);
        // write palette here, we use direct for now so no writing

        DataTypes.writeLongArray(data, buf);
    }

    public void set(int index, int value) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }

        if (value < 0 || value > this.maxEntryValue) {
            throw new IllegalArgumentException("Value cannot be outside of accepted range.");
        }

        int bitIndex = index * this.bitsPerBlock;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerBlock - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        this.data[startIndex] = this.data[startIndex] & ~(this.maxEntryValue << startBitSubIndex) | ((long) value & this.maxEntryValue) << startBitSubIndex;
        if (startIndex != endIndex) {
            int endBitSubIndex = 64 - startBitSubIndex;
            this.data[endIndex] = this.data[endIndex] >>> endBitSubIndex << endBitSubIndex | ((long) value & this.maxEntryValue) >> endBitSubIndex;
        }
    }

    public int get(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }

        int bitIndex = index * this.bitsPerBlock;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerBlock - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        if (startIndex == endIndex) {
            return (int) (this.data[startIndex] >>> startBitSubIndex & this.maxEntryValue);
        } else {
            int endBitSubIndex = 64 - startBitSubIndex;
            return (int) ((this.data[startIndex] >>> startBitSubIndex | this.data[endIndex] << endBitSubIndex) & this.maxEntryValue);
        }
    }

    public boolean isEmpty() {
        return false;
    }
}
