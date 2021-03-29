package io.github.nan8279.smcs.level.physics.random_tick;

import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public interface RandomTick {
    /**
     * Updates a block.
     *
     * @param server the server.
     * @param position the position the block is at.
     */
    void updateBlock(Server server, BlockPosition position);
}
