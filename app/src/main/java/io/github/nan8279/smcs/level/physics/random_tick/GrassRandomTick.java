package io.github.nan8279.smcs.level.physics.random_tick;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public class GrassRandomTick implements RandomTick {

    @Override
    public void updateBlock(Server server, BlockPosition position) {
        BlockPosition posAbove = new BlockPosition(position.getPosX(),
                (short) (position.getPosY() + 1), position.getPosZ());
        Block blockAbove = server.getLevel().getBlock(posAbove);

        if (blockAbove.solid || blockAbove.liquid) {
            server.setBlock(position, Block.DIRT);
        }
    }
}
