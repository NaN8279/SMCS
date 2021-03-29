package io.github.nan8279.smcs.event_manager;

import io.github.nan8279.smcs.event_manager.events.Event;

import java.util.ArrayList;

/**
 * The server's event manager.
 */
public class EventManager {
    final private ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    /**
     * Adds an event handler to the server.
     *
     * @param eventHandler the event handler to add.
     */
    public void addEventHandler(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    /**
     * Runs an event on the server's event handlers.
     *
     * @param event the event.
     */
    public void runEvent(Event event) {
        for (int i = 0; i < eventHandlers.size(); i++) {
            EventHandler eventHandler = eventHandlers.get(i);
            eventHandler.onEvent(event);
        }
    }
}
