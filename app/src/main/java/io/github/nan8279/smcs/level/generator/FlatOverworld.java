package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.Random;

public class FlatOverworld extends FlatWorld {

    @Override
    public ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock,
                                     long seed) {
        ServerLevel level = generateFlatWorld(xSize, ySize, zSize, baseBlock,
                seed);
        int maxOreLevel = (ySize / 2) - 10;
        Random random = new Random(seed);

        Overworld.generateWater(level, ySize / 2);
        Overworld.generateTrees(level, random);
        Overworld.generateFlowerFields(level, random);
        Overworld.generateOres(level, random, maxOreLevel);

        return level;
    }
}
