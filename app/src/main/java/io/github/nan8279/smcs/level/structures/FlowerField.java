package io.github.nan8279.smcs.level.structures;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.blocks.ValidFlowerBlocks;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.HashMap;
import java.util.Random;

public class FlowerField extends StructureGenerator {

    public FlowerField(HashMap<BlockPosition, Block> blocksToGenerate) {
        super(blocksToGenerate);
    }

    public static FlowerField generateFlowerField(Random random, Block flower,
                                                  ServerLevel level, BlockPosition centerPos) {
        HashMap<BlockPosition, Block> blocksToGenerate = new HashMap<>();

        if (!ValidFlowerBlocks.isValidBlock(level.getBlock(centerPos))) {
            return new FlowerField(blocksToGenerate);
        }

        int flowerCount = random.nextInt(10);
        int flowersGenerated = 0;

        for (short x = -5; x < 6; x++) {
            for (short z = -5; z < 6; z++) {
                BlockPosition highestBlock = level.getHighestBlockPosition(
                        centerPos.getPosX() + x,
                        centerPos.getPosZ() + z);

                BlockPosition flowerPosition = new BlockPosition(
                        (short) (centerPos.getPosX() + x),
                        highestBlock.getPosY(),
                        (short) (centerPos.getPosZ() + z));
                if (!ValidFlowerBlocks.isValidBlock(level.getBlock(flowerPosition))) {
                    continue;
                }

                if (flowersGenerated >= flowerCount) {
                    return new FlowerField(blocksToGenerate);
                }

                if (random.nextInt(10) == 5) {
                    blocksToGenerate.put(new BlockPosition(x,
                            (short) (highestBlock.getPosY() - centerPos.getPosY() + 1), z), flower);
                }
                flowerCount++;
            }
        }

        return new FlowerField(blocksToGenerate);
    }
}
