package co.aikar.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Handles registering of commands into brigadier
 *
 * @author MiniDigger
 * @deprecated Unstable API
 */
@Deprecated
@UnstableAPI
public abstract class ACFBrigadierManager<S> implements SuggestionProvider<S> {

    private Logger log = LoggerFactory.getLogger(ACFBrigadierManager.class);

    private CommandManager<?, ?, ?, ?, ?, ?> manager;
    private CommandDispatcher<S> dispatcher;

    private Map<Class<?>, ArgumentType<?>> arguments = new HashMap<>();

    /**
     * Constructs a new brigadier manager, utilizing the currently active command manager and an brigadier provider.
     */
    public ACFBrigadierManager(CommandManager<?, ?, ?, ?, ?, ?> manager, ACFBrigadierProvider provider) {
        this.manager = manager;
        //noinspection unchecked
        this.dispatcher = (CommandDispatcher<S>) provider.getCommandDispatcher();

        manager.verifyUnstableAPI("brigadier");

        // TODO support stuff like min max via brigadier?
        registerArgument(String.class, StringArgumentType.string());
        registerArgument(float.class, FloatArgumentType.floatArg());
        registerArgument(double.class, DoubleArgumentType.doubleArg());
        registerArgument(boolean.class, BoolArgumentType.bool());
        registerArgument(int.class, IntegerArgumentType.integer());
    }

    public <T> void registerArgument(Class<T> clazz, ArgumentType<T> type) {
        arguments.put(clazz, type);
    }

    public void register(RootCommand command) {
        log.debug("* registering command " + command.getCommandName());
        LiteralCommandNode<S> baseCmd = LiteralArgumentBuilder.<S>literal(command.getCommandName()).build();
        RegisteredCommand defaultCommand = null;
        for (Map.Entry<String, RegisteredCommand> entry : command.getSubCommands().entries()) {
            if (entry.getKey().startsWith("__")) {
                if (entry.getKey().equals("__default")) {
                    defaultCommand = entry.getValue();
                }
                // don't register stuff like __catchunknown
                continue;
            }
            log.debug("* * registering subcommand " + entry.getKey());
            LiteralCommandNode<S> subCommandNode = LiteralArgumentBuilder.<S>literal(entry.getKey()).build();
            CommandNode<S> paramNode = subCommandNode;
            for (CommandParameter param : entry.getValue().parameters) {
                if (manager.isCommandIssuer(param.getType()) && !param.getFlags().containsKey("other")) {
                    continue;
                }
                paramNode.addChild(paramNode = RequiredArgumentBuilder.<S, Object>argument(param.getName(), getArgumentTypeByClazz(param.getType())).suggests(this).build());
                log.debug("* * * registering param " + param.getName() + " of type " + param.getType().getSimpleName() + " with argument type " + ((ArgumentCommandNode) paramNode).getType().getClass().
                        getSimpleName());
            }
            baseCmd.addChild(subCommandNode);
            log.debug("* * registered subcommand " + entry.getKey());
        }
        if (defaultCommand != null) {
            CommandNode<S> paramNode = baseCmd;
            for (CommandParameter param : defaultCommand.parameters) {
                if (manager.isCommandIssuer(param.getType()) && !param.getFlags().containsKey("other")) {
                    continue;
                }
                paramNode.addChild(paramNode = RequiredArgumentBuilder.<S, Object>argument(param.getName(), getArgumentTypeByClazz(param.getType())).suggests(this).build());
                log.debug("* * registering param " + param.getName() + " of type " + param.getType().getSimpleName() + " with argument type " + ((ArgumentCommandNode) paramNode).getType().getClass().
                        getSimpleName());
            }
        }

        dispatcher.getRoot().addChild(baseCmd);

        log.debug("* registered " + command.getCommandName());
    }

    private ArgumentType<Object> getArgumentTypeByClazz(Class<?> clazz) {
        //noinspection unchecked
        return (ArgumentType<Object>) arguments.getOrDefault(clazz, StringArgumentType.string());
    }

    @Override
    public abstract CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException;
}
