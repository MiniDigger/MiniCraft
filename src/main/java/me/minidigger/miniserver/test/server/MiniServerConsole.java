package me.minidigger.miniserver.test.server;

import net.minecrell.terminalconsole.SimpleTerminalConsole;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniServerConsole extends SimpleTerminalConsole {

    private static final Logger log = LoggerFactory.getLogger(MiniServerConsole.class);

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
        log.info("Got command {}", command);
        log.trace("Trace");
        log.debug("Debug");
        log.info("Info"  );
        log.warn("Warn" );
        log.error("Error");
    }

    @Override
    protected void shutdown() {
        log.info("Shutting down");
        System.exit(0);
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder
                .appName("MiniServer")
                .completer(new MiniServerCommandCompleter())
        );
    }
}
