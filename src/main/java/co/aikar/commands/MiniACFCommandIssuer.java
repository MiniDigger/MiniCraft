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

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import me.minidigger.minicraft.model.command.CommandSource;

public class MiniACFCommandIssuer implements CommandIssuer {
    private final MiniACFCommandManager manager;
    private final CommandSource sender;

    MiniACFCommandIssuer(MiniACFCommandManager manager, CommandSource sender) {
        this.manager = manager;
        this.sender = sender;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public CommandSource getIssuer() {
        return sender;
    }

    @Override
    public UUID getUniqueId() {
        //generate a unique id based of the name (like for the console command sender)
        return UUID.nameUUIDFromBytes(sender.getName().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public CommandManager getManager() {
        return manager;
    }

    @Override
    public void sendMessageInternal(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String name) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniACFCommandIssuer that = (MiniACFCommandIssuer) o;
        return Objects.equals(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender);
    }
}
