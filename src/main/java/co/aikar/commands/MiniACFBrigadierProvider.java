package co.aikar.commands;

import com.mojang.brigadier.CommandDispatcher;

import me.minidigger.minicraft.model.CommandSource;

public class MiniACFBrigadierProvider implements ACFBrigadierProvider {

    private CommandDispatcher<CommandSource> commandDispatcher = new CommandDispatcher<>();

    @Override
    public Object getCommandDispatcher() {
        return commandDispatcher;
    }
}
