package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;

public interface TerrainGenerator {
    ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock,
                              long seed);
}
