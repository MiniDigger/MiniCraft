package me.minidigger.miniserver.test.model.chunk;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class ChunkPosition {

    private int x;
    private int z;

    public ChunkPosition(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkPosition)) return false;
        ChunkPosition that = (ChunkPosition) o;
        return x == that.x &&
                z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, z);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("z", z)
                .toString();
    }
}
