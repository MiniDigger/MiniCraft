package me.minidigger.minicraft.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import me.minidigger.minicraft.App;
import me.minidigger.minicraft.api.Client;
import me.minidigger.minicraft.model.ServerStatusResponse;
import me.minidigger.minicraft.model.command.CommandSource;
import me.minidigger.minicraft.protocol.MiniPacketHandler;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.*;

public class MiniCraftClient extends App {

    private MiniPacketHandler packetHandler;
    private Client client;

    public MiniCraftClient() {
    }

    public static void main(String[] args) {
        new MiniCraftClient().run();
    }

    public void run() {
        init();

        client = new Client("MiniDigger", this);

        packetHandler = new MiniClientPacketHandler(client);
        packetHandler.init();
    }

    @Override
    public void registerCommands() {
        super.registerCommands();

        BiConsumer<ServerStatusResponse, CommandSource> handle = (r, s) -> {
            s.sendMessage("Response: " + r.getPlayers().getOnline() + "/" + r.getPlayers().getMax());
            if (r.getPlayers().getSample().size() > 0) {
                s.sendMessage("Sample:");
                r.getPlayers().getSample().forEach((ps) -> s.sendMessage(ps.toString()));
            }
            if (r.getRawDescription().length() > 0) {
                s.sendMessage("Motd:");
                s.sendMessage(r.getRawDescription());
            }
        };
        RequiredArgumentBuilder<CommandSource, Integer> portBuilder = RequiredArgumentBuilder.argument("port", integer());
        portBuilder.executes(c -> {
            CompletableFuture<ServerStatusResponse> future = client.doServerListPing(getString(c, "hostname"), getInteger(c, "port"));
            future.thenAcceptAsync((r) -> handle.accept(r, c.getSource()));
            return 1;
        });

        RequiredArgumentBuilder<CommandSource, String> hostNameBuilder = RequiredArgumentBuilder.argument("hostname", string());
        hostNameBuilder.executes(c -> {
            CompletableFuture<ServerStatusResponse> future = client.doServerListPing(getString(c, "hostname"), 25565);
            future.thenAcceptAsync((r) -> handle.accept(r, c.getSource()));
            return 1;
        });

        LiteralArgumentBuilder<CommandSource> pingBuilder = LiteralArgumentBuilder.literal("ping");
        pingBuilder.executes(c -> {
            c.getSource().sendMessage("Invalid args: ");
            c.getSource().sendSmartUsage(getCommandDispatcher(), c.getNodes().get(0).getNode());
            return 1;
        });

        pingBuilder.then(hostNameBuilder.then(portBuilder));
        getCommandDispatcher().register(pingBuilder);
    }

    @Override
    public String getAppName() {
        return "MiniCraftClient";
    }

    @Override
    public MiniPacketHandler getPacketHandler() {
        return packetHandler;
    }
}
