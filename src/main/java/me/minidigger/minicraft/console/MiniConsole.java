package me.minidigger.minicraft.console;

import net.minecrell.terminalconsole.SimpleTerminalConsole;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.aikar.commands.MiniACFCommandManager;
import me.minidigger.minicraft.model.CommandSource;

public class MiniConsole extends SimpleTerminalConsole implements CommandSource {

    private static final Logger log = LoggerFactory.getLogger(MiniConsole.class);

    private String appName;
    private MiniACFCommandManager commandManager;

    public MiniConsole(String appName, MiniACFCommandManager commandManager) {
        this.appName = appName;
        this.commandManager = commandManager;
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
        commandManager.dispatchCommand(this, command);
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
