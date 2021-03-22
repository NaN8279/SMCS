package io.github.nan8279.classic_server.commands.handlers;

import io.github.nan8279.classic_server.commands.CommandException;
import io.github.nan8279.classic_server.commands.CommandHandler;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;

public class BanCommandHandler implements CommandHandler {

    @Override
    public void run(MessageEvent event) throws CommandException {
        if (event.getMessage().split(" ").length != 3) {
            throw new CommandException("Usage: /ban <name> <reason>");
        }

        String name = event.getMessage().split(" ")[1];
        String reason = event.getMessage().split(" ")[2];

        try {
            event.getPlayer().getServer().ban(name, reason);
        } catch (StringToBigToConvertException exception) {
            throw new CommandException("The reason is too big.");
        }

        try {
            event.getPlayer().sendMessage("&cBanned player " + name);
        } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
    }
}
