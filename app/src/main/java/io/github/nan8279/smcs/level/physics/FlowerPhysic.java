package io.github.nan8279.smcs.level.physics;

import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.blocks.ValidFlowerBlocks;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

public class FlowerPhysic implements Physic {

    @Override
    public void updateBlock(SetBlockEvent event) {
        Server server = event.getPlayer().getServer();
        BlockPosition position = event.getBlockPosition();

        BlockPosition positionUnder = new BlockPosition(
                position.getPosX(),
                (short) (position.getPosY() - 1),
                position.getPosZ()
        );
        if (!ValidFlowerBlocks.isValidBlock(server.getLevel().getBlock(positionUnder))) {
            event.setBlock(Block.AIR);
            server.getLevel().updateBlock(new SetBlockEvent(
                    event.getPlayer(),
                    null,
                    positionUnder,
                    server.getLevel().getBlock(positionUnder)
            ));
        }
    }
}
