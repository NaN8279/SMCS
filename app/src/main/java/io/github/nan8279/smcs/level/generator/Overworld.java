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

public class Overworld implements TerrainGenerator {
    final private static ArrayList<Block> ores = new ArrayList<>();
    protected int noiseModifier = 10;

    static void generateFlowerFields(ServerLevel level, Random random) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                BlockPosition highestBlock = level.getHighestBlockPosition(x, z);

                if (random.nextInt(250) == 50) {
                    Block flower = Block.DANDELION;
                    if (random.nextInt(8) == 5) {
                        flower = Block.ROSE;
                    }

                    try {
                        FlowerField.generateFlowerField(random, flower, level, highestBlock).
                                generateStructureForLevel(level, highestBlock, true);
                    } catch (NoSpaceForStructureException ignored) {}
                }
            }
        }
    }

    static void generateTrees(ServerLevel level, Random random) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                BlockPosition highestBlock = level.getHighestBlockPosition(x, z);
                BlockPosition generatePosition = new BlockPosition(
                        highestBlock.getPosX(),
                        (short) (highestBlock.getPosY() + 1),
                        highestBlock.getPosZ());

                if (ValidFlowerBlocks.isValidBlock(level.getBlock(highestBlock))) {
                    if (random.nextInt(150) == 50) {
                        try {
                            new Tree().generateStructureForLevel(level, generatePosition, false);
                        } catch (NoSpaceForStructureException ignored) {}
                    }
                }
            }
        }
    }

    static void generateOres(ServerLevel level, Random random, int maxOreLevel) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                for (short y = 0; y < maxOreLevel; y++) {
                    BlockPosition orePosition = new BlockPosition(x, y, z);

                    if (!(random.nextInt(1000) == 50)) {
                        continue;
                    }

                    if (level.getBlock(orePosition) == Block.STONE) {
                        Block oreBlock = ores.get(random.nextInt(ores.size()));

                        try {
                            OreVein.generateOreVein(random, oreBlock).
                                    generateStructureForLevel(level, orePosition, true);
                        } catch (NoSpaceForStructureException ignored) {}
                    }
                }
            }
        }
    }

    static void generateWater(ServerLevel level, int waterLevel) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                for (short y = 0; y < waterLevel; y++) {
                    BlockPosition position = new BlockPosition(x, y, z);
                    BlockPosition above = new BlockPosition(x, (short) (y + 1), z);

                    if (level.getBlock(position) == Block.AIR) {
                        level.setBlock(position, Block.WATER);
                    } else if (level.getBlock(above) == Block.AIR && y + 1 != waterLevel) {
                        level.setBlock(position, Block.GRAVEL);
                    }
                }
            }
        }
    }

    static void generateGrass(ServerLevel level) {
        for (short x = 0; x < level.getLevelWidth(); x++) {
            for (short z = 0; z < level.getLevelDepth(); z++) {
                BlockPosition position = level.getHighestBlockPosition(x, z);

                if (level.getBlock(position) == Block.DIRT) {
                    level.setBlock(position, Block.GRASS);
                }
            }
        }
    }

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

    static {
        ores.add(Block.GOLD_ORE);
        ores.add(Block.IRON_ORE);
        ores.add(Block.COAL_ORE);
    }

    @Override
    public ServerLevel generateLevel(short xSize, short ySize, short zSize, Block baseBlock, long seed) {
        ServerLevel level = new ServerLevel(new byte[xSize * ySize * zSize], xSize, ySize, zSize, seed);

        Noise noise = new Noise((int) seed);
        noise.SetNoiseType(Noise.NoiseType.Perlin);

        Random random = new Random(seed);
        int maxOreLevel = (ySize / 2) - 10;

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

        generateDirt(level, 3, ySize / 2);
        generateGrass(level);

        generateWater(level, ySize / 2);
        generateTrees(level, random);
        generateFlowerFields(level, random);
        generateOres(level, random, maxOreLevel);

        return level;
    }
}
