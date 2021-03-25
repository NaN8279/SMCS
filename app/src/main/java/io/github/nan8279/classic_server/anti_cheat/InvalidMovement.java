package io.github.nan8279.classic_server.anti_cheat;

import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerMoveEvent;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.position.PlayerPosition;

import java.util.HashMap;

public class InvalidMovement implements EventHandler {
    final private HashMap<Player, Integer> flyTimes = new HashMap<>();

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerMoveEvent) {
            BlockPosition newPosition = BlockPosition.fromPlayerPosition(((PlayerMoveEvent) event).getNewPosition());

            if (isInvalidBlock(event.getPlayer().getServer().getLevel().getBlock(newPosition))) {
                BlockPosition highestBlockPosition = event.getPlayer().getServer().getLevel().
                        getHighestBlockPosition(newPosition.getPosX(), newPosition.getPosZ());

                ((PlayerMoveEvent) event).setNewPosition(
                        PlayerPosition.fromBlockPosition(highestBlockPosition));
            }

            if (isFlying((PlayerMoveEvent) event)) {
                if (flyTimes.containsKey(event.getPlayer())) {
                    flyTimes.replace(event.getPlayer(), flyTimes.get(event.getPlayer()) + 1);
                } else {
                    flyTimes.put(event.getPlayer(), 0);
                }

                if (flyTimes.get(event.getPlayer()) > 10) {
                    BlockPosition highestBlockPosition = event.getPlayer().getServer().getLevel().
                            getHighestBlockPosition(newPosition.getPosX(), newPosition.getPosZ());

                    ((PlayerMoveEvent) event).setNewPosition(
                            PlayerPosition.fromBlockPosition(highestBlockPosition));
                }
            } else {
                if (flyTimes.containsKey(event.getPlayer())) {
                    flyTimes.replace(event.getPlayer(), 0);
                } else {
                    flyTimes.put(event.getPlayer(), 0);
                }
            }

            if (!event.getPlayer().getServer().getLevel().inLevel(newPosition)) {
                ((PlayerMoveEvent) event).setNewPosition(event.getPlayer().getServer().getLevel().getSpawnPos());
            }
        }
    }

    private boolean isInvalidBlock(Block block) {
        return block.solid;
    }

    private boolean isFlying(PlayerMoveEvent event) {
        PlayerPosition newPosition = event.getNewPosition();
        PlayerPosition oldPosition = event.getOldPosition();

        BlockPosition blockUnderPosition = BlockPosition.fromPlayerPosition(newPosition);
        blockUnderPosition = new BlockPosition(
                blockUnderPosition.getPosX(),
                (short) (blockUnderPosition.getPosY() - 1),
                blockUnderPosition.getPosZ());

        if (!event.getPlayer().getServer().getLevel().inLevel(blockUnderPosition)) {
            return false;
        }

        if (event.getPlayer().getServer().getLevel().
                getBlock(BlockPosition.fromPlayerPosition(newPosition)) == Block.WATER) {
            return false;
        }

        if (!event.getPlayer().getServer().getLevel().getBlock(blockUnderPosition).solid) {
            return !(newPosition.getPosY() < oldPosition.getPosY());
        }
        return false;
    }
}
