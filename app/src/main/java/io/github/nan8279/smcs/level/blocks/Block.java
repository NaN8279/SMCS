package io.github.nan8279.smcs.level.blocks;

import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.physics.DoubleSlabPhysic;
import io.github.nan8279.smcs.level.physics.FlowerPhysic;
import io.github.nan8279.smcs.level.physics.Physic;
import io.github.nan8279.smcs.level.physics.SandPhysic;

public enum Block {
    AIR(0, null, false),
    STONE(1),
    GRASS(2),
    DIRT(3),
    COBBLESTONE(4),
    PLANKS(5),
    SAPLING(6, new FlowerPhysic(), false),
    BEDROCK(7),
    WATER(8, null, false),
    STATIONARY_WATER(9, null, false),
    LAVA(10, null, false),
    STATIONARY_LAVA(11, null, false),
    SAND(12, new SandPhysic()),
    GRAVEL(13, new SandPhysic()),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    WOOD(17),
    LEAVES(18),
    SPONGE(19),
    GLASS(20),
    WOOL(36),
    DANDELION(37, new FlowerPhysic(), false),
    ROSE(38, new FlowerPhysic(), false),
    BROWN_MUSHROOM(39, new FlowerPhysic(), false),
    RED_MUSHROOM(40, new FlowerPhysic(), false),
    GOLD_BLOCK(41),
    IRON_BLOCK(42),
    DOUBLE_SLAB(43),
    SLAB(44, new DoubleSlabPhysic(), false),
    BRICKS(45),
    TNT(46),
    BOOKSHELF(47),
    MOSSY_COBBLESTONE(48),
    OBSIDIAN(49);

    final public byte blockID;
    final public Physic physic;
    final public boolean solid;

    Block(int blockID, Physic physic, boolean solid) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        this.solid = solid;
    }

    Block(int blockID, Physic physic) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        solid = true;
    }

    Block(int blockID) {
        this.blockID = (byte) blockID;
        this.physic = null;
        solid = true;
    }

    public static Block fromID(int blockID) throws InvalidBlockIDException {
        for(Block block : Block.values()) {
            if (block.blockID == blockID) {
                return block;
            }
        }
        throw new InvalidBlockIDException();
    }
}
