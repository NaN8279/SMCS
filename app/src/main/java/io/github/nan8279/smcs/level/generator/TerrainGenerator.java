package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

public interface TerrainGenerator {
    ServerLevel generateLevel(BlockPosition spawnPosition, short xSize, short ySize, short zSize, Block baseBlock,
                              long seed);
}
