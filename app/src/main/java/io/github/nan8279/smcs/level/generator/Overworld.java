package io.github.nan8279.smcs.level.generator;

import io.github.nan8279.smcs.exceptions.NoSpaceForStructureException;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.blocks.ValidFlowerBlocks;
import io.github.nan8279.smcs.level.generator.noise.Noise;
import io.github.nan8279.smcs.level.structures.FlowerField;
import io.github.nan8279.smcs.level.structures.OreVein;
import io.github.nan8279.smcs.level.structures.Tree;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.ArrayList;
import java.util.Random;

/**
 * Overworld terrain generator.
 */
public class Overworld implements TerrainGenerator {
    final private static ArrayList<Block> ores = new ArrayList<>();
    protected Block liquid = Block.WATER;
    protected int noiseModifier = 10;
    protected int treeGenerateChance = 150;
    protected Block grassBlock = Block.GRASS;

    /**
     * Generates flower fields at random positions in the level.
     *
     * @param level the level.
     * @param random the random number generator.
     * @param x the x position of the flower field.
     * @param z the z position of the flower field.
     */
    static void generateFlowerFields(ServerLevel level, Random random,
                                     short x, short z) {
        BlockPosition highestBlock = level.getHighestBlockPosition(x, z);

        if (random.nextInt(250) == 0) {
            Block flower = Block.DANDELION;
            if (random.nextInt(8) == 0) {
                flower = Block.ROSE;
            }

            try {
                FlowerField.generateFlowerField(random, flower, level, highestBlock).
                        generateStructureForLevel(level, highestBlock, true);
            } catch (NoSpaceForStructureException ignored) {}
        }
    }

    /**
     * Generates trees at random positions in the level.
     *
     * @param level the level.
     * @param random the random number generator.
     * @param x the x position of the tree.
     * @param z the z position of the tree.
     * @param treeGenerateChance the chance of generating a tree.
     */
    static void generateTrees(ServerLevel level, Random random,
                              short x, short z, int treeGenerateChance) {
        BlockPosition highestBlock = level.getHighestBlockPosition(x, z);
        BlockPosition generatePosition = new BlockPosition(
                highestBlock.getPosX(),
                (short) (highestBlock.getPosY() + 1),
                highestBlock.getPosZ());

        if (ValidFlowerBlocks.isValidBlock(level.getBlock(highestBlock))) {
            if (random.nextInt(treeGenerateChance) == 0) {
                try {
                    new Tree().generateStructureForLevel(level, generatePosition, false);
                } catch (NoSpaceForStructureException ignored) {}
            }
        }
    }

    /**
     * Generates ore veins at random positions in the level.
     *
     * @param level the level.
     * @param random the random number generator.
     * @param x the x position of the ore vein.
     * @param y the y position of the ore vein.
     * @param z the z position of the ore vein.
     */
    static void generateOres(ServerLevel level, Random random,
                             short x, short y, short z) {
        BlockPosition orePosition = new BlockPosition(x, y, z);

        if (!(random.nextInt(1000) == 0)) {
            return;
        }

        if (level.getBlock(orePosition) == Block.STONE) {
            Block oreBlock = ores.get(random.nextInt(ores.size()));

            try {
                OreVein.generateOreVein(random, oreBlock).
                        generateStructureForLevel(level, orePosition, true);
            } catch (NoSpaceForStructureException ignored) {}
        }
    }

    /**
     * Generates water in the level.
     *
     * @param level the level.
     * @param waterLevel the water level.
     * @param liquid the liquid of the level.
     */
    static void generateLiquid(ServerLevel level, int waterLevel, Block liquid) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                for (short y = 0; y < waterLevel; y++) {
                    BlockPosition position = new BlockPosition(x, y, z);
                    BlockPosition above = new BlockPosition(x, (short) (y + 1), z);

                    if (level.getBlock(position) == Block.AIR) {
                        level.setBlock(position, liquid);
                    } else if (level.getBlock(above) == Block.AIR && y + 1 != waterLevel) {
                        level.setBlock(position, Block.GRAVEL);
                    }
                }
            }
        }
    }

    /**
     * Generates grass in the level.
     *
     * @param level the level.
     * @param grassBlock the grass block.
     */
    static void generateGrass(ServerLevel level, Block grassBlock) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                BlockPosition position = level.getHighestBlockPosition(x, z);

                if (level.getBlock(position) == Block.DIRT) {
                    level.setBlock(position, grassBlock);
                }
            }
        }
    }

    /**
     * Generates dirt in the level.
     *
     * @param level the level.
     * @param dirtLevel how deep the dirt goes.
     * @param waterLevel the water level.
     */
    static void generateDirt(ServerLevel level, int dirtLevel, int waterLevel) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                BlockPosition position = level.getHighestBlockPosition(x, z);

                if (position.getPosY() < waterLevel - 1) {
                    continue;
                }

                for (short y = (short) (position.getPosY() - dirtLevel);
                     y < position.getPosY() + 1; y++) {
                    level.setBlock(new BlockPosition(x, y, z), Block.DIRT);
                }
            }
        }
    }

    /**
     * Adds noise to a level.
     *
     * @param level the level.
     * @param seed the seed.
     * @param xSize the size of the level.
     * @param ySize the size of the level.
     * @param zSize the size of the level.
     * @param baseBlock the block used as base in the level.
     */
    protected void setNoise(ServerLevel level, long seed,
                            int xSize, int ySize, int zSize, Block baseBlock) {
        Noise noise = new Noise((int) seed);
        noise.SetNoiseType(Noise.NoiseType.Perlin);

        for (short x = 0; x < xSize; x++) {
            for (short z = 0; z < zSize; z++) {
                int height = (int) ((ySize / 2) + (noise.GetNoise(x, z) * noiseModifier) + 3);

                for (short y = 0; y < height; y++) {
                    try {
                        level.setBlock(new BlockPosition(x, y, z), baseBlock);
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
            }
        }
    }

    /**
     * Generates extra structures in a level.
     *
     * @param random the random number generator.
     * @param level the level.
     */
    protected void generateExtra(Random random, ServerLevel level) {}

    @Override
    public ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock, long seed) {
        ServerLevel level = new ServerLevel(new byte[xSize * ySize * zSize], xSize, ySize, zSize, seed,
                this);

        Random random = new Random(seed);
        int maxOreLevel = (ySize / 2) - 10;

        setNoise(level, seed, xSize, ySize, zSize, baseBlock);

        generateDirt(level, 3, ySize / 2);
        generateGrass(level, grassBlock);
        generateLiquid(level, ySize / 2, liquid);

        for (short x = 0; x < xSize; x++) {
            for (short z = 0; z < zSize; z++) {
                generateTrees(level, random, x, z, treeGenerateChance);
                generateFlowerFields(level, random, x, z);

                for (short y = 0; y < maxOreLevel; y++) {
                    generateOres(level, random, x, y, z);
                }
            }
        }

        generateExtra(random, level);

        return level;
    }

    static {
        ores.add(Block.GOLD_ORE);
        ores.add(Block.IRON_ORE);
        ores.add(Block.COAL_ORE);
    }
}
