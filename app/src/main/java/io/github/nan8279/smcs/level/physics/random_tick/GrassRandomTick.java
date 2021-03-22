package io.github.nan8279.smcs.level.physics.random_tick;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public class GrassRandomTick implements RandomTick {

    @Override
    public void updateBlock(Server server, BlockPosition position) {
        BlockPosition blockAbove = new BlockPosition(position.getPosX(),
                (short) (position.getPosY() + 1), position.getPosZ());

        if (server.getLevel().getBlock(blockAbove).solid) {
            server.setBlock(position, Block.DIRT);
        }
    }
}
