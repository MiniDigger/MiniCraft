package me.minidigger.minicraft.model;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;

import java.util.Collection;

public interface CommandSource {

    String getName();

    void sendMessage(String message);

    default void sendSmartUsage(CommandDispatcher<CommandSource> dispatcher, CommandNode<CommandSource> node) {
        Collection<String> usage = dispatcher.getSmartUsage(node, this).values();
        for (String line : usage) {
            sendMessage("* " + line);
        }
    }

    default void sendAllUsage(CommandDispatcher<CommandSource> dispatcher, CommandNode<CommandSource> node) {
        String[] usage = dispatcher.getAllUsage(node, this, false);
        for (String line : usage) {
            sendMessage("* " + line);
        }
    }
}
