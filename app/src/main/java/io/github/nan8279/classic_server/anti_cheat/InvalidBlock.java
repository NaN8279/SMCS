package io.github.nan8279.classic_server.anti_cheat;

import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.ArrayList;

public class InvalidBlock implements EventHandler {
    final private static ArrayList<Block> invalidBlocks = new ArrayList<>();

    @Override
    public void onEvent(Event event) {
        if (event instanceof SetBlockEvent) {
            BlockPosition playerPosition = BlockPosition.fromPlayerPosition(event.getPlayer().getPos());
            BlockPosition blockPosition = ((SetBlockEvent) event).getBlockPosition();

            int xDistance = Math.abs(playerPosition.getPosX() - blockPosition.getPosX());
            int yDistance = Math.abs(playerPosition.getPosY() - blockPosition.getPosY());
            int zDistance = Math.abs(playerPosition.getPosZ() - blockPosition.getPosZ());

            if (xDistance > 5 || yDistance > 5 || zDistance > 5) {
                ((SetBlockEvent) event).setCanceled(true);
            }

            if (invalidBlocks.contains(((SetBlockEvent) event).getBlock())) {
                ((SetBlockEvent) event).setCanceled(true);
            }
        }
    }

    static {
        invalidBlocks.add(Block.DOUBLE_SLAB);
        invalidBlocks.add(Block.LAVA);
        invalidBlocks.add(Block.WATER);
        invalidBlocks.add(Block.STATIONARY_LAVA);
        invalidBlocks.add(Block.STATIONARY_WATER);
        invalidBlocks.add(Block.BEDROCK);
        invalidBlocks.add(Block.GRASS);
    }
}
