package me.minidigger.minicraft.api;

import com.google.common.base.MoreObjects;

import net.kyori.text.Component;

import java.util.UUID;

import me.minidigger.minicraft.model.ChatPosition;
import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.client.ClientPlayChatMessage;
import me.minidigger.minicraft.protocol.client.ClientPlayKeepAlive;

public class Player {

    private UUID uuid;
    private String name;
    private String brand;

    private MiniConnection connection;

    public MiniConnection getConnection() {
        return connection;
    }

    public void setConnection(MiniConnection connection) {
        this.connection = connection;
    }

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

    public void tick() {
        connection.sendPacket(new ClientPlayKeepAlive());
    }

    public void sendMessage(Component component) {
        sendMessage(component, ChatPosition.CHAT);
    }

    public void sendMessage(Component component, ChatPosition position) {
        ClientPlayChatMessage packet = new ClientPlayChatMessage();
        packet.setComponent(component);
        packet.setPosition(position);
        getConnection().sendPacket(packet);
    }
}
