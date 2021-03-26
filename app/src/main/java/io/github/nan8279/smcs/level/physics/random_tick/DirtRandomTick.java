package io.github.nan8279.smcs.level.physics.random_tick;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public class DirtRandomTick implements RandomTick {

    @Override
    public void updateBlock(Server server, BlockPosition position) {
        BlockPosition posAbove = new BlockPosition(position.getPosX(),
                (short) (position.getPosY() + 1), position.getPosZ());
        Block blockAbove = server.getLevel().getBlock(posAbove);

        if (blockAbove.solid || blockAbove.liquid) {
            return;
        }

        int x = position.getPosX();
        int y = position.getPosY();
        int z = position.getPosZ();

        for (short newX = (short) (x - 2); newX < x + 2; newX++) {
            for (short newY = (short) (y - 2); newY < y + 2; newY++) {
                for (short newZ = (short) (z - 2); newZ < z + 2; newZ++) {
                    BlockPosition newBlockPosition = new BlockPosition(newX, newY, newZ);

                    if (server.getLevel().getBlock(newBlockPosition) == Block.GRASS) {
                        server.setBlock(position, Block.GRASS);
                        return;
                    }
                }
            }
        }
    }
}
