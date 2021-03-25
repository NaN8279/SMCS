package io.github.nan8279.smcs.level.structures;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.HashMap;
import java.util.Random;

public class OreVein extends StructureGenerator {

    public OreVein(HashMap<BlockPosition, Block> blocksToGenerate) {
        super(blocksToGenerate);
    }

    public static OreVein generateOreVein(Random random, Block ore) {
        int oreCount = random.nextInt(8);
        int oresGenerated = 0;
        HashMap<BlockPosition, Block> blocksToGenerate = new HashMap<>();

        for (short x = -2; x < 3; x++) {
            for (short y = -2; y < 3; y++) {
                for (short z = -2; z < 3; z++) {
                    if (oresGenerated >= oreCount) {
                        return new OreVein(blocksToGenerate);
                    }

                    if (random.nextInt(6) == 5) {
                        blocksToGenerate.put(new BlockPosition(x, y, z), ore);
                    }
                    oreCount++;
                }
            }
        }

        return new OreVein(blocksToGenerate);
    }
}
