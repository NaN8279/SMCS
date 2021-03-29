package io.github.nan8279.smcs.level.structures;

import io.github.nan8279.smcs.exceptions.NoSpaceForStructureException;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

import java.util.HashMap;

/**
 * Structure generator. Used to generate structures like trees in levels.
 */
public abstract class StructureGenerator {
    final private HashMap<BlockPosition, Block> blocksToGenerate;

    public StructureGenerator(HashMap<BlockPosition, Block> blocksToGenerate) {
        this.blocksToGenerate = blocksToGenerate;
    }

    /**
     * Test if this structure can generate at the given position in the given level.
     *
     * @param level the level.
     * @param position the position.
     * @param canSpawnInBlocks if this structure can spawn in blocks.
     * @return true when the structure can spawn.
     */
    private boolean canGenerateStructure(ServerLevel level, BlockPosition position,
                                         boolean canSpawnInBlocks) {
        for (BlockPosition relativeStructurePosition : blocksToGenerate.keySet()) {
            BlockPosition structurePosition = new BlockPosition(
                    (short) (position.getPosX() + relativeStructurePosition.getPosX()),
                    (short) (position.getPosY() + relativeStructurePosition.getPosY()),
                    (short) (position.getPosZ() + relativeStructurePosition.getPosZ()));

            if (structurePosition.getPosX() == position.getPosX() &&
                    structurePosition.getPosY() == position.getPosY() &&
                    structurePosition.getPosZ() == position.getPosZ()) {
                continue;
            }

            if (!canSpawnInBlocks) {
                if (level.getBlock(structurePosition) != Block.AIR) {
                    return false;
                }
            }

            if (!level.inLevel(structurePosition)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Generates this structure on a server.
     *
     * @param server the server.
     * @param position the position to generate this structure at.
     * @param canSpawnInBlocks if this structure can spawn in blocks.
     * @throws NoSpaceForStructureException when there is no space for this structure to generate.
     */
    public void generateStructure(Server server, BlockPosition position,
                                  boolean canSpawnInBlocks) throws NoSpaceForStructureException {
        if (!canGenerateStructure(server.getLevel(), position, canSpawnInBlocks)) {
            throw new NoSpaceForStructureException();
        }

        for (BlockPosition relativeStructurePosition : blocksToGenerate.keySet()) {
            BlockPosition structurePosition = new BlockPosition(
                    (short) (position.getPosX() + relativeStructurePosition.getPosX()),
                    (short) (position.getPosY() + relativeStructurePosition.getPosY()),
                    (short) (position.getPosZ() + relativeStructurePosition.getPosZ()));

            server.setBlock(structurePosition, blocksToGenerate.get(relativeStructurePosition));
        }
    }

    /**
     * Generates this structure in a level.
     *
     * @param level the level.
     * @param position the position to generate this structure at.
     * @param canSpawnInBlocks if this structure can spawn in blocks.
     * @throws NoSpaceForStructureException when there is no space for this structure to generate.
     */
    public void generateStructureForLevel(ServerLevel level, BlockPosition position,
                                          boolean canSpawnInBlocks)
            throws NoSpaceForStructureException {
        if (!canGenerateStructure(level, position, canSpawnInBlocks)) {
            throw new NoSpaceForStructureException();
        }

        for (BlockPosition relativeStructurePosition : blocksToGenerate.keySet()) {
            BlockPosition structurePosition = new BlockPosition(
                    (short) (position.getPosX() + relativeStructurePosition.getPosX()),
                    (short) (position.getPosY() + relativeStructurePosition.getPosY()),
                    (short) (position.getPosZ() + relativeStructurePosition.getPosZ()));

            level.setBlock(structurePosition, blocksToGenerate.get(relativeStructurePosition));
        }
    }
}
