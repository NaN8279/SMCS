package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.BlockPosition;

/**
 * Occurs when a block has been placed orr broken.
 */
public class SetBlockEvent extends Event {
    private BlockPosition blockPosition;
    private boolean canceled = false;
    private Block block;

    /**
     * @param blockPosition the block position.
     * @param block the new block.
     */
    public SetBlockEvent(Player player, ClientBoundPacket packet,
                         BlockPosition blockPosition, Block block) {
        super(player, packet);
        this.blockPosition = blockPosition;
        this.block = block;
    }

    /**
     * @return the block position.
     */
    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    /**
     * @return the new block.
     *
     * The old block can be acquired by using {@link io.github.nan8279.smcs.level.ServerLevel#getBlock(BlockPosition)}.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * @param position sets the block position.
     */
    public void setBlockPosition(BlockPosition position) {
        this.blockPosition = position;
    }

    /**
     * @param block sets the block.
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * @return if the event has been canceled.
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * @param canceled if the event must be canceled.
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
