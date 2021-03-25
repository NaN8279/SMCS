package io.github.nan8279.smcs.level.blocks;

import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.physics.DoubleSlabPhysic;
import io.github.nan8279.smcs.level.physics.FlowerPhysic;
import io.github.nan8279.smcs.level.physics.Physic;
import io.github.nan8279.smcs.level.physics.SandPhysic;
import io.github.nan8279.smcs.level.physics.random_tick.DirtRandomTick;
import io.github.nan8279.smcs.level.physics.random_tick.GrassRandomTick;
import io.github.nan8279.smcs.level.physics.random_tick.RandomTick;
import io.github.nan8279.smcs.level.physics.random_tick.SaplingRandomTick;

public enum Block {
    AIR(0, null, false),
    STONE(1),
    GRASS(2, null, true, new GrassRandomTick()),
    DIRT(3, null, true, new DirtRandomTick()),
    COBBLESTONE(4),
    PLANKS(5),
    SAPLING(6, new FlowerPhysic(), false, new SaplingRandomTick()),
    BEDROCK(7),
    WATER(8, null, true),
    STATIONARY_WATER(9, null, true),
    LAVA(10, null, true),
    STATIONARY_LAVA(11, null, true),
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

    final public RandomTick randomTick;
    final public Physic physic;
    final public boolean solid;
    final public byte blockID;

    Block(int blockID, Physic physic, boolean solid, RandomTick randomTick) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        this.solid = solid;
        this.randomTick = randomTick;
    }

    Block(int blockID, Physic physic, boolean solid) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        this.solid = solid;
        this.randomTick = null;
    }

    Block(int blockID, Physic physic) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        solid = true;
        this.randomTick = null;
    }

    Block(int blockID) {
        this.blockID = (byte) blockID;
        this.physic = null;
        solid = true;
        this.randomTick = null;
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
