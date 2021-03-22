package io.github.nan8279.classic_server.commands;

import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;

public class MessageHandler implements EventHandler {

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageEvent) {
            if (((MessageEvent) event).getMessage().startsWith("/")) {
                ((MessageEvent) event).setCanceled(true);
                Commands.executeCommand((MessageEvent) event);
            }
        }
    }
}
