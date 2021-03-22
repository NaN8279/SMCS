package io.github.nan8279.smcs.event_manager;

import io.github.nan8279.smcs.event_manager.events.Event;

public interface EventHandler {
    void onEvent(Event event);
}
