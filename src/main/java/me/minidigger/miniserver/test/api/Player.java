package me.minidigger.miniserver.test.api;

import com.google.common.base.MoreObjects;

import java.util.UUID;

public class Player {

    private UUID uuid;
    private String name;
    private String brand;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("uuid", uuid)
                .add("name", name)
                .add("brand", brand)
                .toString();
    }
}
