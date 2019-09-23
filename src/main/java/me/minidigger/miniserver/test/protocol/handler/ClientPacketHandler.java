package me.minidigger.miniserver.test.protocol.handler;

import me.minidigger.miniserver.test.protocol.server.ServerHandshake;
import me.minidigger.miniserver.test.protocol.server.ServerLoginEncryptionResponse;
import me.minidigger.miniserver.test.protocol.server.ServerLoginStart;
import me.minidigger.miniserver.test.protocol.server.ServerPlayBlockPlace;
import me.minidigger.miniserver.test.protocol.server.ServerPlayChatMessage;
import me.minidigger.miniserver.test.protocol.server.ServerPlayClientSettings;
import me.minidigger.miniserver.test.protocol.server.ServerPlayKeepAlive;
import me.minidigger.miniserver.test.protocol.server.ServerPlayLook;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPlayerAbilities;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPluginMessage;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPosition;
import me.minidigger.miniserver.test.protocol.server.ServerPlayPositionAndLook;
import me.minidigger.miniserver.test.protocol.server.ServerPlayTeleportConfirm;
import me.minidigger.miniserver.test.protocol.server.ServerStatusPing;
import me.minidigger.miniserver.test.protocol.server.ServerStatusRequest;
import me.minidigger.miniserver.test.netty.MiniConnection;

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
