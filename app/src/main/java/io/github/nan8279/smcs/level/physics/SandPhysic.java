package io.github.nan8279.smcs.level.physics;

import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

public class SandPhysic implements Physic {
    @Override
    public void updateBlock(SetBlockEvent event) {
        BlockPosition blockBelow = new BlockPosition(
                event.getBlockPosition().getPosX(),
                (short) (event.getBlockPosition().getPosY() - 1),
                event.getBlockPosition().getPosZ()
        );

        if (event.getPlayer().getServer().getLevel().getBlock(blockBelow) != Block.AIR) {
            return;
        }

        event.getPlayer().getServer().setBlock(event.getBlockPosition(), Block.AIR);

        BlockPosition newBlock = event.getPlayer().getServer().getLevel().getHighestBlockPosition(
                event.getBlockPosition().getPosX(),
                event.getBlockPosition().getPosZ(),
                event.getBlockPosition().getPosY() + 1
        );

        newBlock = new BlockPosition(newBlock.getPosX(), (short) (newBlock.getPosY() + 1), newBlock.getPosZ());
        event.setBlockPosition(newBlock);
    }
}
