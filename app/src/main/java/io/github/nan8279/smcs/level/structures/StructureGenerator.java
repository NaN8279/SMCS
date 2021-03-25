package io.github.nan8279.smcs.level.structures;

import io.github.nan8279.smcs.exceptions.NoSpaceForStructureException;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

import java.util.HashMap;

public abstract class StructureGenerator {
    final private HashMap<BlockPosition, Block> blocksToGenerate;

    public StructureGenerator(HashMap<BlockPosition, Block> blocksToGenerate) {
        this.blocksToGenerate = blocksToGenerate;
    }

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
