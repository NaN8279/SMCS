package io.github.nan8279.smcs.level.physics;

import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public class DoubleSlabPhysic implements Physic {

    @Override
    public void updateBlock(SetBlockEvent event) {
        Server server = event.getPlayer().getServer();
        BlockPosition position = event.getBlockPosition();
        if (position.getPosY() == 0) {
            return;
        }

        BlockPosition positionUnder = new BlockPosition(
                position.getPosX(),
                (short) (position.getPosY() - 1),
                position.getPosZ()
        );

        if (server.getLevel().getBlock(positionUnder) == Block.SLAB) {
            server.setBlock(positionUnder, Block.DOUBLE_SLAB);
            event.setBlock(Block.AIR);
        }
    }
}
