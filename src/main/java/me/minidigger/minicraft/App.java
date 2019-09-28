package me.minidigger.minicraft;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.minidigger.minicraft.console.MiniConsole;
import me.minidigger.minicraft.model.command.CommandSource;
import me.minidigger.minicraft.protocol.MiniPacketRegistry;
import me.minidigger.minicraft.protocol.MiniPacketHandler;

public abstract class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private CommandDispatcher<CommandSource> commandDispatcher;
    private MiniConsole console;
    private MiniPacketRegistry packetRegistry;

    public abstract String getAppName();

    public abstract MiniPacketHandler getPacketHandler();

    public void init() {
        log.info("Starting  {}, hello friend!", getAppName());

        commandDispatcher = new CommandDispatcher<>();

        console = new MiniConsole(getAppName(), getCommandDispatcher());
        console.start();

        packetRegistry = new MiniPacketRegistry();
        packetRegistry.init();

        registerCommands();
    }

    public void registerCommands() {
        getCommandDispatcher().register(LiteralArgumentBuilder.<CommandSource>literal("info").executes(c -> {
            c.getSource().sendMessage("You are using " + getAppName() + ", woooo!");
            return 1;
        }));
        getCommandDispatcher().register(LiteralArgumentBuilder.<CommandSource>literal("close").executes(c -> {
            c.getSource().sendMessage("Shutting down " + getAppName() + ", bye!");
            System.exit(0);
            return 1;
        }));
        getCommandDispatcher().register(LiteralArgumentBuilder.<CommandSource>literal("help").executes(c -> {
            c.getSource().sendMessage("You have access to the following commands: ");
            c.getSource().sendSmartUsage(getCommandDispatcher(), getCommandDispatcher().getRoot());
            c.getSource().sendAllUsage(getCommandDispatcher(), getCommandDispatcher().getRoot());
            return 1;
        }));
    }

    public CommandDispatcher<CommandSource> getCommandDispatcher() {
        return commandDispatcher;
    }

    public MiniConsole getConsole() {
        return console;
    }

    public MiniPacketRegistry getPacketRegistry() {
        return packetRegistry;
    }
}
