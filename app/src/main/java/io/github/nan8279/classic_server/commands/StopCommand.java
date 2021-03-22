package io.github.nan8279.classic_server.commands;

import io.github.nan8279.smcs.event_manager.events.MessageEvent;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", true);
    }

    @Override
    public void run(MessageEvent event) {
        event.getPlayer().getServer().stop();
    }
}
