package io.github.nan8279.smcs.event_manager;

import io.github.nan8279.smcs.event_manager.events.Event;

/**
 * An event handler.
 */
public interface EventHandler {
    /**
     * Executed when an event occurs on the server.
     *
     * @param event the event that occurs.
     */
    void onEvent(Event event);
}
