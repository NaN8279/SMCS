package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

/**
 * Flat world terrain generator.
 */
public class FlatWorld implements TerrainGenerator {

    /**
     * Generates a level containing a big cube.
     *
     * @param xSize the size of the level.
     * @param ySize the size of the level.
     * @param zSize the size of the level.
     * @param baseBlock the block the cube is made of.
     * @param seed the seed of the level.
     * @return the generated level.
     */
    protected ServerLevel generateFlatWorld(short xSize, short ySize, short zSize, Block baseBlock, long seed) {
        byte[] worldData = new byte[xSize * ySize * zSize];
        ServerLevel level = new ServerLevel(worldData, xSize, ySize, zSize, seed, this);

        for (short x = 0; x < xSize; x++) {
            for (short y = 0; y < (ySize / 2); y++) {
                for (short z = 0; z < zSize; z++) {
                    BlockPosition position = new BlockPosition(x, y, z);
                    level.setBlock(position, baseBlock);
                }
            }
        }

        Overworld.generateDirt(level, 3, 0);
        Overworld.generateGrass(level, Block.GRASS);

        return level;
    }

    @Override
    public ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock,
                                     long seed) {
        return generateFlatWorld(xSize, ySize, zSize, baseBlock, seed);
    }
}
