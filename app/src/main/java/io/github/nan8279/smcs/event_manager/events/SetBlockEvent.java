package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.BlockPosition;

public class SetBlockEvent extends Event {
    private BlockPosition blockPosition;
    private boolean canceled = false;
    private Block block;

    public SetBlockEvent(Player player, ClientBoundPacket packet,
                         BlockPosition blockPosition, Block block) {
        super(player, packet);
        this.blockPosition = blockPosition;
        this.block = block;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlockPosition(BlockPosition position) {
        this.blockPosition = position;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setBlock(int id) throws InvalidBlockIDException {
        this.block = Block.fromID(id);
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
