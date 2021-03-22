package io.github.nan8279.classic_server.commands.handlers;

import io.github.nan8279.classic_server.commands.CommandHandler;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;

public class StopCommandHandler implements CommandHandler {

    @Override
    public void run(MessageEvent event) {
        event.getPlayer().getServer().stop();
    }
}
