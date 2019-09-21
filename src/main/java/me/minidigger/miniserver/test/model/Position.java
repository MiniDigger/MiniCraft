package me.minidigger.miniserver.test.model;

import com.google.common.base.MoreObjects;

public class Position {

    private double x, y, z;
    private float pitch, yaw;

    public Position(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    public Position(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .add("pitch", pitch)
                .add("yaw", yaw)
                .toString();
    }
}
