package io.github.nan8279.smcs.level.physics.random_tick;

import io.github.nan8279.smcs.exceptions.NoSpaceForStructureException;
import io.github.nan8279.smcs.level.structures.Tree;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public class SaplingRandomTick implements RandomTick {

    @Override
    public void updateBlock(Server server, BlockPosition position) {
        try {
            new Tree().generateStructure(server, position);
        } catch (NoSpaceForStructureException ignored) {}
    }
}
