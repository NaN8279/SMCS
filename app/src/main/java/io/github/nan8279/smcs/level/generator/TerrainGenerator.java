package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;

public interface TerrainGenerator {
    /**
     * Generates a level.
     *
     * @param xSize the size of the level.
     * @param ySize the size of the level.
     * @param zSize the size of the level.
     * @param baseBlock the block used as base in the level.
     * @param seed the seed of the level.
     * @return the generated level.
     */
    ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock,
                              long seed);
}
