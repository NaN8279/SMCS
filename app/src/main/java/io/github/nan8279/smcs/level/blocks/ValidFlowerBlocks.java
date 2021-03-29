package io.github.nan8279.smcs.level.blocks;

/**
 * Lists valid blocks to plant a flower on.
 */
public enum ValidFlowerBlocks {
    GRASS(Block.GRASS),
    DIRT(Block.DIRT);

    final public Block block;
    ValidFlowerBlocks(Block block) {
        this.block = block;
    }

    /**
     * @param block the block to look for.
     * @return true if the block is a valid block to plant a flower on.
     */
    public static boolean isValidBlock(Block block) {
        for (ValidFlowerBlocks flower : ValidFlowerBlocks.values()) {
            if (flower.block == block) {
                return true;
            }
        }
        return false;
    }
}
