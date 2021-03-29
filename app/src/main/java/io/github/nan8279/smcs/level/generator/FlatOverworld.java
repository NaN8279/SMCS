package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;

import java.util.Random;

/**
 * Flat overworld terrain generator.
 */
public class FlatOverworld extends FlatWorld {

    @Override
    public ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock,
                                     long seed) {
        ServerLevel level = generateFlatWorld(xSize, ySize, zSize, baseBlock,
                seed);
        int maxOreLevel = (ySize / 2) - 10;
        Random random = new Random(seed);

        Overworld.generateLiquid(level, ySize / 2, Block.WATER);

        for (short x = 0; x < xSize; x++) {
            for (short z = 0; z < zSize; z++) {
                Overworld.generateTrees(level, random, x, z, 150);
                Overworld.generateFlowerFields(level, random, x, z);

                for (short y = 0; y < maxOreLevel; y++) {
                    Overworld.generateOres(level, random, x, y, z);
                }
            }
        }

        return level;
    }
}
