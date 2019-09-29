package me.minidigger.minicraft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.MiniACFCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import me.minidigger.minicraft.console.MiniConsole;
import me.minidigger.minicraft.model.CommandSource;
import me.minidigger.minicraft.protocol.MiniPacketHandler;
import me.minidigger.minicraft.protocol.MiniPacketRegistry;

public abstract class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private MiniConsole console;
    private MiniPacketRegistry packetRegistry;
    private MiniACFCommandManager commandManager;

    public abstract String getAppName();

    public abstract MiniPacketHandler getPacketHandler();

    public void init() {
        log.info("Starting  {}, hello friend!", getAppName());

        commandManager = new MiniACFCommandManager();

        console = new MiniConsole(getAppName(), getCommandManager());
        console.start();

        packetRegistry = new MiniPacketRegistry();
        packetRegistry.init();

        registerCommands();
    }

    public void registerCommands() {
        getCommandManager().registerCommand(new Commands());
    }

    class Commands extends BaseCommand {
        @CommandAlias("info")
        @Description("Shows information about this application")
        public void info(CommandSource source) {
            source.sendMessage("You are using " + getAppName() + ", woooo!");
        }

        @CommandAlias("close")
        @Description("Safely closes this application")
        public void close(CommandSource source) {
            source.sendMessage("Shutting down " + getAppName() + ", bye!");
            System.exit(0);
        }

        @CommandAlias("help")
        @Description("Show a list with all commands")
        public void help(CommandSource source) {
            source.sendMessage("You have access to the following commands: ");
            getCommandManager().sendHelp(source);
        }

        @CommandAlias("test")
        @Description("Just a brigadier test command")
        public void test(CommandSource source, String someArg, @Default("2") int someOptionalArg) {
            source.sendMessage("Cool, you entered " + someArg + " and " + someOptionalArg);
        }
    }

    public MiniConsole getConsole() {
        return console;
    }

    public MiniPacketRegistry getPacketRegistry() {
        return packetRegistry;
    }

    public MiniACFCommandManager getCommandManager() {
        return commandManager;
    }
}
