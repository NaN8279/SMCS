package io.github.nan8279.smcs.level.physics.random_tick;

import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public interface RandomTick {
    void updateBlock(Server server, BlockPosition position);
}
