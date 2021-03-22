package io.github.nan8279.smcs.event_manager;

import io.github.nan8279.smcs.event_manager.events.Event;

import java.util.ArrayList;

public class EventManager {
    final private ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    public void addEventHandler(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void runEvent(Event event) {
        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.onEvent(event);
        }
    }
}
