package co.aikar.commands;

import com.mojang.brigadier.CommandDispatcher;

import me.minidigger.minicraft.model.command.CommandSource;

public class MiniACFBrigadierProvider implements ACFBrigadierProvider {

    private CommandDispatcher<CommandSource> commandDispatcher = new CommandDispatcher<>();

    @Override
    public Object getCommandDispatcher() {
        return commandDispatcher;
    }
}
