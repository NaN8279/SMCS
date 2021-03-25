package io.github.nan8279.smcs.level.blocks;

public enum ValidFlowerBlocks {
    GRASS(Block.GRASS),
    DIRT(Block.DIRT);

    final public Block block;
    ValidFlowerBlocks(Block block) {
        this.block = block;
    }

    public static boolean isValidBlock(Block block) {
        for (ValidFlowerBlocks flower : ValidFlowerBlocks.values()) {
            if (flower.block == block) {
                return true;
            }
        }
        return false;
    }
}
