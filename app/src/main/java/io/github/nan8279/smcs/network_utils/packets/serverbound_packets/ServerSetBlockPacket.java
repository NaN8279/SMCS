package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.ArrayList;

public class ServerSetBlockPacket implements ServerBoundPacket {
    final private BlockPosition blockPosition;
    final private Block block;

    public ServerSetBlockPacket(BlockPosition position, Block block) {
        blockPosition = position;
        this.block = block;
    }

    @Override
    public byte returnPacketID() {
        return 6;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add(NetworkUtils.shortToBytes(blockPosition.getPosX())[0]);
        packet.add(NetworkUtils.shortToBytes(blockPosition.getPosX())[1]);

        packet.add(NetworkUtils.shortToBytes(blockPosition.getPosY())[0]);
        packet.add(NetworkUtils.shortToBytes(blockPosition.getPosY())[1]);

        packet.add(NetworkUtils.shortToBytes(blockPosition.getPosZ())[0]);
        packet.add(NetworkUtils.shortToBytes(blockPosition.getPosZ())[1]);

        packet.add(block.blockID);

        return packet;
    }
}
