package io.github.nan8279.smcs.level.physics;

import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;

public interface Physic {
    void updateBlock(SetBlockEvent event);
}
