package co.aikar.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

import me.minidigger.minicraft.model.CommandSource;

public class MiniACFBrigadierManager extends ACFBrigadierManager<CommandSource> {

    public MiniACFBrigadierManager(CommandManager<?, ?, ?, ?, ?, ?> manager, ACFBrigadierProvider provider) {
        super(manager, provider);
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        return null;
    }
}
