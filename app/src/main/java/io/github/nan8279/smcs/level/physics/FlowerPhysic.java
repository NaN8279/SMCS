package io.github.nan8279.smcs.level.physics;

import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

enum Flower {
    SAPLING(Block.SAPLING),
    DANDELION(Block.DANDELION),
    ROSE(Block.ROSE),
    BROWN_MUSHROOM(Block.BROWN_MUSHROOM),
    RED_MUSHROOM(Block.RED_MUSHROOM);

    final public Block block;
    Flower(Block block) {
        this.block = block;
    }

    public static boolean isFlower(Block block) {
        for (Flower flower : Flower.values()) {
            if (flower.block == block) {
                return true;
            }
        }
        return false;
    }
}

public class FlowerPhysic implements Physic {

    @Override
    public SetBlockEvent updateBlock(SetBlockEvent event) {
        Server server = event.getPlayer().getServer();
        BlockPosition position = event.getBlockPosition();

        BlockPosition positionUnder = new BlockPosition(
                position.getPosX(),
                (short) (position.getPosY() - 1),
                position.getPosZ()
        );
        if (Flower.isFlower(server.getLevel().getBlock(positionUnder))) {
            event.setBlock(Block.AIR);
            server.getLevel().checkPhysic(new SetBlockEvent(
                    event.getPlayer(),
                    null,
                    positionUnder,
                    server.getLevel().getBlock(positionUnder)
            ));
        }
        return event;
    }
}
