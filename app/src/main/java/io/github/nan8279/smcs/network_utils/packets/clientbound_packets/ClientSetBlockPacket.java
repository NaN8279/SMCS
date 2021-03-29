package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.ClientPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.position.BlockPosition;

/**
 * Client set block packet.
 */
public class ClientSetBlockPacket implements ClientBoundPacket {
    private BlockPosition blockPosition;
    private byte mode;
    private Block blockHolding;

    @Override
    public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
        short x = clientPacket.readShort();
        short y = clientPacket.readShort();
        short z = clientPacket.readShort();

        blockPosition = new BlockPosition(x, y, z);

        mode = clientPacket.readByte();
        try {
            byte id = clientPacket.readByte();
            blockHolding = Block.fromID(id);
        } catch (InvalidBlockIDException exception) {
            throw new InvalidPacketException();
        }
    }

    /**
     * @return the block the player is holding.
     */
    public Block getBlockHolding() {
        return blockHolding;
    }

    /**
     * @return the block position the player is targeting.
     */
    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    /**
     * @return 1 if the player is placing the block.
     */
    public byte getMode() {
        return mode;
    }
}
