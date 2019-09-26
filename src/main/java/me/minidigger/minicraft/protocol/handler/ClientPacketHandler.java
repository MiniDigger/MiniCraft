package me.minidigger.minicraft.protocol.handler;

import me.minidigger.minicraft.netty.MiniConnection;
import me.minidigger.minicraft.protocol.server.ServerHandshake;
import me.minidigger.minicraft.protocol.server.ServerPlayBlockPlace;
import me.minidigger.minicraft.protocol.server.ServerPlayPlayerAbilities;
import me.minidigger.minicraft.protocol.server.ServerPlayTeleportConfirm;
import me.minidigger.minicraft.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.minicraft.protocol.server.ServerLoginStart;
import me.minidigger.minicraft.protocol.server.ServerPlayChatMessage;
import me.minidigger.minicraft.protocol.server.ServerPlayClientSettings;
import me.minidigger.minicraft.protocol.server.ServerPlayKeepAlive;
import me.minidigger.minicraft.protocol.server.ServerPlayLook;
import me.minidigger.minicraft.protocol.server.ServerPlayPluginMessage;
import me.minidigger.minicraft.protocol.server.ServerPlayPosition;
import me.minidigger.minicraft.protocol.server.ServerPlayPositionAndLook;
import me.minidigger.minicraft.protocol.server.ServerStatusPing;
import me.minidigger.minicraft.protocol.server.ServerStatusRequest;

public abstract class ClientPacketHandler implements PacketHandler {

    @Override
    public void handle(MiniConnection connection, ServerHandshake packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerStatusRequest packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerStatusPing packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerLoginStart packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerLoginEncryptionResponse packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPluginMessage packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayChatMessage packet) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayKeepAlive serverPlayKeepAlive) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayClientSettings serverPlayClientSettings) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayTeleportConfirm serverPlayTeleportConfirm) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPositionAndLook serverPlayPositionAndLook) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPosition serverPlayPosition) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayLook serverPlayLook) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayPlayerAbilities serverPlayPlayerAbilities) {

    }

    @Override
    public void handle(MiniConnection connection, ServerPlayBlockPlace serverPlayBlockPlace) {

    }
}
