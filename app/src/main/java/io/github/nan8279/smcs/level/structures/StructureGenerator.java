package io.github.nan8279.smcs.level.structures;

import io.github.nan8279.smcs.exceptions.NoSpaceForStructureException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

import java.util.HashMap;

public abstract class StructureGenerator {
    final private HashMap<BlockPosition, Block> blocksToGenerate;

    public StructureGenerator(HashMap<BlockPosition, Block> blocksToGenerate) {
        this.blocksToGenerate = blocksToGenerate;
    }

    public void generateStructure(Server server, BlockPosition position) throws NoSpaceForStructureException {
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

            if (server.getLevel().getBlock(structurePosition) != Block.AIR) {
                throw new NoSpaceForStructureException();
            }
        }

        for (BlockPosition relativeStructurePosition : blocksToGenerate.keySet()) {
            BlockPosition structurePosition = new BlockPosition(
                    (short) (position.getPosX() + relativeStructurePosition.getPosX()),
                    (short) (position.getPosY() + relativeStructurePosition.getPosY()),
                    (short) (position.getPosZ() + relativeStructurePosition.getPosZ()));

            server.setBlock(structurePosition, blocksToGenerate.get(relativeStructurePosition));
        }
    }
}
