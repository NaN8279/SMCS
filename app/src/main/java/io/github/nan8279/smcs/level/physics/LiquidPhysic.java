package io.github.nan8279.smcs.level.physics;

import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.ArrayList;

public class LiquidPhysic implements Physic {
    final private int updateDelay;

    public LiquidPhysic(int updateDelay) {
        this.updateDelay = updateDelay;
    }

    @Override
    public void updateBlock(SetBlockEvent event) {
        for (BlockPosition newWaterPosition : getNewWaterPositions(event.getBlockPosition())) {
            if (event.getPlayer().getServer().getLevel().inLevel(newWaterPosition)) {
                if (event.getPlayer().getServer().getLevel().getBlock(newWaterPosition) == Block.AIR) {
                    event.getPlayer().getServer().addDelayedBlockUpdate(
                            new SetBlockEvent(
                                    event.getPlayer(),
                                    event.getPacket(),
                                    newWaterPosition,
                                    event.getBlock()
                            ),
                            updateDelay
                    );
                    event.getPlayer().getServer().setBlock(newWaterPosition, event.getBlock());
                }
            }
        }
    }

    private static ArrayList<BlockPosition> getNewWaterPositions(BlockPosition position) {
        ArrayList<BlockPosition> newWaterPositions = new ArrayList<>();

        newWaterPositions.add(new BlockPosition(
                (short) (position.getPosX() - 1),
                position.getPosY(),
                position.getPosZ()
        ));

        newWaterPositions.add(new BlockPosition(
                (short) (position.getPosX() + 1),
                position.getPosY(),
                position.getPosZ()
        ));

        newWaterPositions.add(new BlockPosition(
                position.getPosX(),
                position.getPosY(),
                (short) (position.getPosZ() - 1)
        ));

        newWaterPositions.add(new BlockPosition(
                position.getPosX(),
                position.getPosY(),
                (short) (position.getPosZ() + 1)
        ));

        newWaterPositions.add(new BlockPosition(
                position.getPosX(),
                (short) (position.getPosY() - 1),
                position.getPosZ()
        ));

        return newWaterPositions;
    }
}
