/*
 * Copyright (c) 2016-2017 Daniel Ennis (Aikar) - MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package co.aikar.commands;

import net.kyori.text.format.TextColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import co.aikar.commands.apachecommonslang.ApacheCommonsExceptionUtil;
import me.minidigger.minicraft.model.command.CommandSource;

@SuppressWarnings("WeakerAccess")
public class MiniACFCommandManager extends CommandManager<
        CommandSource,
        MiniACFCommandIssuer,
        TextColor,
        MiniACFMessageFormatter,
        MiniACFCommandExecutionContext,
        MiniACFConditionContext
        > {

    @SuppressWarnings("WeakerAccess")
    private static final Logger logger = LoggerFactory.getLogger(MiniACFCommandManager.class);
    protected Map<String, MiniACFRootCommand> registeredCommands = new HashMap<>();
    protected MiniACFCommandContexts contexts;
    protected MiniACFCommandCompletions completions;
    protected MiniACFLocales locales;
    private ACFBrigadierManager<CommandSource> brigadierManager;

    @SuppressWarnings("JavaReflectionMemberAccess")
    public MiniACFCommandManager() {
        this.formatters.put(MessageType.ERROR, defaultFormatter = new MiniACFMessageFormatter(TextColor.RED, TextColor.YELLOW, TextColor.RED));
        this.formatters.put(MessageType.SYNTAX, new MiniACFMessageFormatter(TextColor.YELLOW, TextColor.GREEN, TextColor.WHITE));
        this.formatters.put(MessageType.INFO, new MiniACFMessageFormatter(TextColor.BLUE, TextColor.DARK_GREEN, TextColor.GREEN));
        this.formatters.put(MessageType.HELP, new MiniACFMessageFormatter(TextColor.AQUA, TextColor.GREEN, TextColor.YELLOW));
        getLocales(); // auto load locales

        enableUnstableAPI("help");
        enableUnstableAPI("brigadier");
        this.brigadierManager = new MiniACFBrigadierManager(this, new MiniACFBrigadierProvider());
    }

    @Override
    public boolean isCommandIssuer(Class<?> type) {
        return CommandSource.class.isAssignableFrom(type);
    }

    @Override
    public synchronized CommandContexts<MiniACFCommandExecutionContext> getCommandContexts() {
        if (this.contexts == null) {
            this.contexts = new MiniACFCommandContexts(this);
        }
        return contexts;
    }

    @Override
    public synchronized CommandCompletions<MiniACFCommandCompletionContext> getCommandCompletions() {
        if (this.completions == null) {
            this.completions = new MiniACFCommandCompletions(this);
        }
        return completions;
    }


    @Override
    public MiniACFLocales getLocales() {
        if (this.locales == null) {
            this.locales = new MiniACFLocales(this);
            this.locales.loadLanguages();
        }
        return locales;
    }


    @Override
    public boolean hasRegisteredCommands() {
        return !registeredCommands.isEmpty();
    }

    public void registerCommand(BaseCommand command, boolean force) {
        command.onRegister(this);
        for (Map.Entry<String, RootCommand> entry : command.registeredCommands.entrySet()) {
            String commandName = entry.getKey().toLowerCase();
            MiniACFRootCommand bukkitCommand = (MiniACFRootCommand) entry.getValue();
            registeredCommands.put(commandName, bukkitCommand);
            brigadierManager.register(bukkitCommand);
        }
    }

    @Override
    public void registerCommand(BaseCommand command) {
        registerCommand(command, false);
    }

    @Override
    public RootCommand createRootCommand(String cmd) {
        return new MiniACFRootCommand(this, cmd);
    }

    @Override
    public Collection<RootCommand> getRegisteredRootCommands() {
        return Collections.unmodifiableCollection(registeredCommands.values());
    }

    @Override
    public MiniACFCommandIssuer getCommandIssuer(Object issuer) {
        if (!(issuer instanceof CommandSource)) {
            throw new IllegalArgumentException(issuer.getClass().getName() + " is not a Command Issuer.");
        }
        return new MiniACFCommandIssuer(this, (CommandSource) issuer);
    }

    @Override
    public MiniACFCommandExecutionContext createCommandContext(RegisteredCommand command, CommandParameter parameter, CommandIssuer sender, List<String> args, int i, Map<String, Object> passedArgs) {
        return new MiniACFCommandExecutionContext(command, parameter, (MiniACFCommandIssuer) sender, args, i, passedArgs);
    }

    @Override
    public MiniACFCommandCompletionContext createCompletionContext(RegisteredCommand command, CommandIssuer sender, String input, String config, String[] args) {
        return new MiniACFCommandCompletionContext(command, (MiniACFCommandIssuer) sender, input, config, args);
    }

    @Override
    public RegisteredCommand createRegisteredCommand(BaseCommand command, String cmdName, Method method, String prefSubCommand) {
        return new MiniACFRegisteredCommand(command, cmdName, method, prefSubCommand);
    }

    @Override
    public MiniACFConditionContext createConditionContext(CommandIssuer issuer, String config) {
        return new MiniACFConditionContext((MiniACFCommandIssuer) issuer, config);
    }


    @Override
    public void log(LogLevel level, String message, Throwable throwable) {
        if (level == LogLevel.INFO) {
            logger.info(LogLevel.LOG_PREFIX + message);
        } else {
            logger.error(LogLevel.LOG_PREFIX + message);
        }
        if (throwable != null) {
            for (String line : ACFPatterns.NEWLINE.split(ApacheCommonsExceptionUtil.getFullStackTrace(throwable))) {
                if (level == LogLevel.INFO) {
                    logger.info(LogLevel.LOG_PREFIX + line);
                } else {
                    logger.error(LogLevel.LOG_PREFIX + line);
                }
            }
        }
    }

    @Override
    public String getCommandPrefix(CommandIssuer issuer) {
        return issuer.isPlayer() ? "/" : "";
    }

    public void dispatchCommand(CommandSource source, String msg) {
        CommandIssuer issuer = this.getCommandIssuer(source);

        String prefixFound = null;
        if (!msg.startsWith(prefixFound = getCommandPrefix(issuer))) {
            return;
        }

        String[] args = ACFPatterns.SPACE.split(msg.substring(prefixFound.length()), -1);
        if (args.length == 0) {
            return;
        }
        String cmd = args[0].toLowerCase();
        MiniACFRootCommand rootCommand = this.registeredCommands.get(cmd);
        if (rootCommand == null) {
            return;
        }
        if (args.length > 1) {
            args = Arrays.copyOfRange(args, 1, args.length);
        } else {
            args = new String[0];
        }
        rootCommand.execute(issuer, cmd, args);
    }

    public void sendHelp(CommandSource source) {
        CommandIssuer issuer = getCommandIssuer(source);
        List<HelpEntry> helpEntries = new ArrayList<>();
        for (MiniACFRootCommand cmd : registeredCommands.values()) {
            CommandHelp commandHelp = generateCommandHelp(issuer, cmd);
            helpEntries.addAll(commandHelp.getHelpEntries());
        }

        MiniACFRootCommand fakeCommand = new MiniACFRootCommand(this, "all commands");
        fakeCommand.addChild(new BaseCommand() {
        });

        CommandHelp aggregated = new CommandHelp(this, fakeCommand, issuer);
        aggregated.getHelpEntries().clear();
        aggregated.getHelpEntries().addAll(helpEntries);
        aggregated.showHelp(issuer);
    }
}
