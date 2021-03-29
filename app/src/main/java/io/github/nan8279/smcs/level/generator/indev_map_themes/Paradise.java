package io.github.nan8279.smcs.level.generator.indev_map_themes;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.generator.Overworld;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.Random;

/**
 * Indev paradise map theme.
 */
public class Paradise extends Overworld {

    private void generateBeaches(ServerLevel level, int waterLevel) {
        for (int x = 0; x < level.getLevelWidth(); x++) {
            for (int z = 0; z < level.getLevelDepth(); z++) {
                BlockPosition highestBlock = level.getHighestBlockPosition(x, z,
                        waterLevel + 1);
                if (level.getBlock(highestBlock) == Block.GRASS) {
                    level.setBlock(highestBlock, Block.SAND);
                }
            }
        }
    }

    @Override
    protected void generateExtra(Random random, ServerLevel level) {
        int beachLevel = (level.getLevelHeight() / 2) + 2;

        generateBeaches(level, beachLevel);
    }
}
