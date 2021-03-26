package io.github.nan8279.smcs.level.blocks;

import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.physics.*;
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
    SAPLING(6, new FlowerPhysic(), false, true, false,
            true, new SaplingRandomTick()),
    BEDROCK(7),
    WATER(8, new LiquidPhysic(10), false, true, true,
            false, null),
    STATIONARY_WATER(9, new LiquidPhysic(10), false, true, true,
            false, null),
    LAVA(10, new LiquidPhysic(20), false, true, true,
            false, null),
    STATIONARY_LAVA(11, new LiquidPhysic(20), false, true, true,
            false, null),
    SAND(12, new SandPhysic()),
    GRAVEL(13, new SandPhysic()),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    WOOD(17),
    LEAVES(18),
    SPONGE(19),
    GLASS(20),
    RED_CLOTH(21),
    ORANGE_CLOTH(22),
    YELLOW_CLOTH(23),
    CHARTREUSE_CLOTH(24),
    GREEN_CLOTH(25),
    SPRING_GREEN_CLOTH(26),
    CYAN_CLOTH(27),
    CAPRI_CLOTH(28),
    ULTRAMARINE_CLOTH(29),
    VIOLET_CLOTH(30),
    PURPLE_CLOTH(31),
    MAGENTA_CLOTH(32),
    ROSE_CLOTH(33),
    DARK_GRAY_CLOTH(34),
    LIGHT_GRAY_CLOTH(35),
    WHITE_CLOTH(36),
    DANDELION(37, new FlowerPhysic(), false, false, false,
            true, null),
    ROSE(38, new FlowerPhysic(), false, false, false,
            true, null),
    BROWN_MUSHROOM(39, new FlowerPhysic(), false, false, false,
            true, null),
    RED_MUSHROOM(40, new FlowerPhysic(), false, false, false,
            true, null),
    GOLD_BLOCK(41),
    IRON_BLOCK(42),
    DOUBLE_SLAB(43),
    SLAB(44, new DoubleSlabPhysic(), false, true, false,
            false, null),
    BRICKS(45),
    TNT(46),
    BOOKSHELF(47),
    MOSSY_COBBLESTONE(48),
    OBSIDIAN(49);

    final public RandomTick randomTick;
    final public Physic physic;
    final public boolean solid;
    final public boolean blocksLight;
    final public boolean liquid;
    final public boolean small;
    final public byte blockID;

    Block(int blockID, Physic physic, boolean solid, boolean blocksLight, boolean liquid,
          boolean small, RandomTick randomTick) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        this.solid = solid;
        this.randomTick = randomTick;
        this.blocksLight = blocksLight;
        this.liquid = liquid;
        this.small = small;
    }

    Block(int blockID, Physic physic, boolean solid, RandomTick randomTick) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        this.solid = solid;
        this.randomTick = randomTick;
        this.blocksLight = solid;
        this.liquid = false;
        this.small = false;
    }

    Block(int blockID, Physic physic, boolean solid) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        this.solid = solid;
        this.randomTick = null;
        this.blocksLight = solid;
        this.liquid = false;
        this.small = false;
    }

    Block(int blockID, Physic physic) {
        this.blockID = (byte) blockID;
        this.physic = physic;
        solid = true;
        this.randomTick = null;
        this.blocksLight = solid;
        this.liquid = false;
        this.small = false;
    }

    Block(int blockID) {
        this.blockID = (byte) blockID;
        this.physic = null;
        solid = true;
        this.randomTick = null;
        this.blocksLight = solid;
        this.liquid = false;
        this.small = false;
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
