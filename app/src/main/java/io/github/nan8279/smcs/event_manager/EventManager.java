package io.github.nan8279.smcs.event_manager;

import io.github.nan8279.smcs.event_manager.events.Event;

import java.util.ArrayList;

public class EventManager {
    final private ArrayList<EventHandler> eventHandlers = new ArrayList<>();

    public void addEventHandler(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void runEvent(Event event) {
        for (int i = 0; i < eventHandlers.size(); i++) {
            EventHandler eventHandler = eventHandlers.get(i);
            eventHandler.onEvent(event);
        }
    }
}
