package me.minidigger.minicraft.console;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecrell.terminalconsole.SimpleTerminalConsole;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.minidigger.minicraft.model.command.CommandSource;

public class MiniConsole extends SimpleTerminalConsole implements CommandSource {

    private static final Logger log = LoggerFactory.getLogger(MiniConsole.class);

    private String appName;
    private CommandDispatcher<CommandSource> dispatcher;

    public MiniConsole(String appName, CommandDispatcher<CommandSource> commandDispatcher) {
        this.appName = appName;
        this.dispatcher = commandDispatcher;
    }

    @Override
    public void start() {
        new Thread(super::start, "TerminalThread").start();
    }

    @Override
    protected boolean isRunning() {
        return true;
    }

    @Override
    protected void runCommand(String command) {
        log.debug("Got command {}", command);
        try {
            int i = dispatcher.execute(command, this);
        } catch (CommandSyntaxException e) {
            log.error("Invalid command: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error while executing command {}", command, e);
        }
    }

    @Override
    protected void shutdown() {
        log.info("Shutting down");
        System.exit(0);
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder
                .appName(appName)
                .completer(new MiniCommandCompleter())
        );
    }

    @Override
    public String getName() {
        return appName;
    }

    @Override
    public void sendMessage(String message) {
        log.info(message);
    }
}
