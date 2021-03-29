package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.position.BlockPosition;

/**
 * Server set block packet.
 */
public class ServerSetBlockPacket implements ServerBoundPacket {
    final private BlockPosition blockPosition;
    final private Block block;

    /**
     * @param position the block position that is being set.
     * @param block the new block.
     */
    public ServerSetBlockPacket(BlockPosition position, Block block) {
        blockPosition = position;
        this.block = block;
    }

    @Override
    public byte returnPacketID() {
        return 6;
    }

    @Override
    public ServerPacket returnPacket() {
        ServerPacket packet = new ServerPacket();

        packet.addShort(blockPosition.getPosX());
        packet.addShort(blockPosition.getPosY());
        packet.addShort(blockPosition.getPosZ());
        packet.addByte(block.blockID);

        return packet;
    }
}
