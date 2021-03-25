package io.github.nan8279.classic_server.commands.handlers;

import io.github.nan8279.classic_server.commands.CommandException;
import io.github.nan8279.classic_server.commands.CommandHandler;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;

public class SeedCommandHandler implements CommandHandler {

    @Override
    public void run(MessageEvent event) throws CommandException {
        try {
            event.getPlayer().sendMessage("Seed: " + event.getPlayer().getServer().getLevel().getSeed());
        } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
    }
}
