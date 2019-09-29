package me.minidigger.minicraft.client;

import java.util.concurrent.CompletableFuture;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import me.minidigger.minicraft.model.ServerStatusResponse;
import me.minidigger.minicraft.model.command.CommandSource;

public class MiniClientCommandHandler extends BaseCommand {

    private MiniCraftClient client;

    public MiniClientCommandHandler(MiniCraftClient client) {
        this.client = client;
    }

    @CommandAlias("ping")
    class Ping extends BaseCommand {

        @Default
        @Description("Does a server list ping to the given server")
        public void ping(CommandSource source, String hostname, @Default("25565") int port) {
            CompletableFuture<ServerStatusResponse> future = client.getClient().doServerListPing(hostname, port);
            future.thenAcceptAsync((r) -> {
                source.sendMessage("Response: " + r.getPlayers().getOnline() + "/" + r.getPlayers().getMax());
                if (r.getPlayers().getSample().size() > 0) {
                    source.sendMessage("Sample:");
                    r.getPlayers().getSample().forEach((ps) -> source.sendMessage(ps.toString()));
                }
                if (r.getRawDescription().length() > 0) {
                    source.sendMessage("Motd:");
                    source.sendMessage(r.getRawDescription());
                }
            });
        }
    }

    @CommandAlias("connect")
    class Connect extends BaseCommand {

        @Default
        @Description("Connects to the given server")
        public void connect(CommandSource source, String hostname, @Default("25565") int port) {
            client.getClient().connect(hostname, port, (con) -> {
                source.sendMessage("Connected");
            });
        }
    }
}
