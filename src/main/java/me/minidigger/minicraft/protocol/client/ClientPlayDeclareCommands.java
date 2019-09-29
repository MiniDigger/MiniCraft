package me.minidigger.minicraft.protocol.client;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import co.aikar.commands.ACFBrigadierManager;
import co.aikar.commands.MiniACFBrigadierManager;
import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.CommandSource;
import me.minidigger.minicraft.model.Key;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ClientPlayDeclareCommands extends MiniPacket {

    private static final Logger log = LoggerFactory.getLogger(ClientPlayDeclareCommands.class);

    private RootCommandNode<CommandSource> rootCommandNode;

    public void setRootCommandNode(RootCommandNode<CommandSource> rootCommandNode) {
        this.rootCommandNode = rootCommandNode;
    }

    private Map<CommandNode<CommandSource>, Integer> generateCommandMap() {
        // walk thru the command tree and assign indices
        Map<CommandNode<CommandSource>, Integer> commandMap = Maps.newHashMap();
        Deque<CommandNode<CommandSource>> queue = new ArrayDeque<>();
        queue.add(this.rootCommandNode);

        while (!queue.isEmpty()) {
            CommandNode<CommandSource> node = queue.pollFirst();
            if (!commandMap.containsKey(node)) {
                commandMap.put(node, commandMap.size());
                queue.addAll(node.getChildren());
                if (node.getRedirect() != null) {
                    queue.add(node.getRedirect());
                }
            }
        }

        return commandMap;
    }

    @Override
    public void toWire(ByteBuf buf) {
        Map<CommandNode<CommandSource>, Integer> commandMap = generateCommandMap();

        // convert the map into an array
        @SuppressWarnings("unchecked")  // generic arrays pls...
                CommandNode<CommandSource>[] nodes = new CommandNode[commandMap.size()];
        for (Map.Entry<CommandNode<CommandSource>, Integer> entry : commandMap.entrySet()) {
            nodes[entry.getValue()] = entry.getKey();
        }

        // write the array
        DataTypes.writeVarInt(nodes.length, buf);
        for (CommandNode<CommandSource> node : nodes) {
            this.writeCommandNode(buf, node, commandMap);
        }

        // write the index of the root
        DataTypes.writeVarInt(commandMap.get(this.rootCommandNode), buf);
    }

    private void writeCommandNode(ByteBuf buf, CommandNode<CommandSource> node, Map<CommandNode<CommandSource>, Integer> commandMap) {
        // calc flag and write it
        byte flag = 0;
        if (node.getRedirect() != null) {
            flag = (byte) (flag | 8);
        }

        if (node.getCommand() != null) {
            flag = (byte) (flag | 4);
        }

        if (node instanceof RootCommandNode) {
            flag = (byte) (flag | 0);
        } else if (node instanceof ArgumentCommandNode) {
            flag = (byte) (flag | 2);
//            if (((ArgumentCommandNode) node).getCustomSuggestions() != null) {
//                flag = (byte) (flag | 16);
//            }
        } else {
            if (!(node instanceof LiteralCommandNode)) {
                throw new UnsupportedOperationException("Unknown node type " + node);
            }

            flag = (byte) (flag | 1);
        }
        buf.writeByte(flag);

        // write childs
        DataTypes.writeVarInt(node.getChildren().size(), buf);
        for (CommandNode<CommandSource> childNode : node.getChildren()) {
            DataTypes.writeVarInt(commandMap.get(childNode), buf);
        }

        // write redirect
        if (node.getRedirect() != null) {
            DataTypes.writeVarInt(commandMap.get(node.getRedirect()), buf);
        }

        // write argument name or literal name
        if (node instanceof ArgumentCommandNode) {
            ArgumentCommandNode<CommandSource, ?> argumentCommandNode = (ArgumentCommandNode<CommandSource, ?>) node;
            DataTypes.writeString(argumentCommandNode.getName(), buf);
            DataTypes.writeString(ACFBrigadierManager.getArgumentKey(argumentCommandNode.getType().getClass()), buf);
            if(argumentCommandNode.getType().getClass().getName().contains("StringArgument")) {
                DataTypes.writeVarInt(0, buf); //TODO refactor this
            } else {
                buf.writeByte(0);
            }
//            if (argumentCommandNode.getCustomSuggestions() != null) {
//                if (argumentCommandNode.getCustomSuggestions() instanceof MiniACFBrigadierManager) {
//                    DataTypes.writeString("minecraft:ask_server", buf);
//                } else {
//                    log.warn("unknown custom suggestions {}", argumentCommandNode.getCustomSuggestions());
//                }
//            }
        } else if (node instanceof LiteralCommandNode) {
            DataTypes.writeString(((LiteralCommandNode) node).getLiteral(), buf);
        }
    }


    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("nodes", generateCommandMap().size())
                .toString();
    }
}
