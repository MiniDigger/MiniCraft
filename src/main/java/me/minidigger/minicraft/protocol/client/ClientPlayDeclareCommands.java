package me.minidigger.minicraft.protocol.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import me.minidigger.minicraft.model.command.CommandNode;
import me.minidigger.minicraft.protocol.DataTypes;
import me.minidigger.minicraft.protocol.MiniPacket;

public class ClientPlayDeclareCommands extends MiniPacket {

    private int rootIndex;
    private List<CommandNode> nodes;

    @Override
    public void toWire(ByteBuf buf) {
        DataTypes.writeVarInt(nodes.size(), buf);
        // write nodes
        DataTypes.writeVarInt(rootIndex, buf);
    }

    @Override
    public void fromWire(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return null;
    }
}
