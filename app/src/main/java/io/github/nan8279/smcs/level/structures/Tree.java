package io.github.nan8279.smcs.level.structures;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.HashMap;

/**
 * Tree structure.
 */
public class Tree extends StructureGenerator {
    final private static HashMap<BlockPosition, Block> tree = new HashMap<>();

    public Tree() {
        super(tree);
    }

    static {
        for (short i = 0; i < 4; i++) {
            tree.put(new BlockPosition((short) 0, i, (short) 0), Block.WOOD);
        }

        for (short x = -1; x < 2; x++) {
            for (short y = 2; y < 4; y++) {
                for (short z = -1; z < 2; z++) {
                    if (x == 0 && z == 0) {
                        continue;
                    }

                    tree.put(new BlockPosition(x, y, z), Block.LEAVES);
                }
            }
        }

        tree.put(new BlockPosition((short) -1, (short) 4, (short) 0), Block.LEAVES);
        tree.put(new BlockPosition((short) 1, (short) 4, (short) 0), Block.LEAVES);
        tree.put(new BlockPosition((short) 0, (short) 4, (short) 0), Block.LEAVES);
        tree.put(new BlockPosition((short) 0, (short) 4, (short) -1), Block.LEAVES);
        tree.put(new BlockPosition((short) 0, (short) 4, (short) 1), Block.LEAVES);
    }
}
