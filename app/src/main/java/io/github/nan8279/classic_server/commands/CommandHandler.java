package io.github.nan8279.classic_server.commands;

import io.github.nan8279.smcs.event_manager.events.MessageEvent;

public interface CommandHandler {
    void run(MessageEvent event) throws CommandException;
}
