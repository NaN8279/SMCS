package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.Packet;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.position.BlockPosition;

public class ClientSetBlockPacket implements ClientBoundPacket {
    private BlockPosition blockPosition;
    private byte mode;
    private Block blockHolding;

    @Override
    public void fromPacket(Packet packet) throws InvalidPacketException {
        short x = packet.readShort();
        short y = packet.readShort();
        short z = packet.readShort();

        blockPosition = new BlockPosition(x, y, z);

        mode = packet.readByte();
        try {
            byte id = packet.readByte();
            blockHolding = Block.fromID(id);
        } catch (InvalidBlockIDException exception) {
            throw new InvalidPacketException();
        }
    }

    public Block getBlockHolding() {
        return blockHolding;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public byte getMode() {
        return mode;
    }
}
