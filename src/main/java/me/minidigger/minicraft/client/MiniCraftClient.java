package me.minidigger.minicraft.client;

import me.minidigger.minicraft.App;
import me.minidigger.minicraft.api.Client;
import me.minidigger.minicraft.protocol.MiniPacketHandler;

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

        getCommandManager().registerCommand(new MiniClientCommandHandler(this));
    }

    @Override
    public String getAppName() {
        return "MiniCraftClient";
    }

    @Override
    public MiniPacketHandler getPacketHandler() {
        return packetHandler;
    }

    public Client getClient() {
        return client;
    }
}
