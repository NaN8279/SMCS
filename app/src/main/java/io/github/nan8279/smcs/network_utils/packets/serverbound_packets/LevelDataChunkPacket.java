package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Level data chunk packet.
 */
public class LevelDataChunkPacket implements ServerBoundPacket {
    final private short chunkLength;
    final private byte[] chunkData;
    final private byte percentComplete;

    /**
     * @param chunkLength the length of the chunk.
     * @param chunkData the data of the chunk.
     * @param percentComplete how much percent is complete in the level loading process.
     */
    public LevelDataChunkPacket(short chunkLength, byte[] chunkData, byte percentComplete) {
        this.chunkLength = chunkLength;
        this.chunkData = chunkData;
        this.percentComplete = percentComplete;
    }

    @Override
    public byte returnPacketID() {
        return 3;
    }

    @Override
    public ServerPacket returnPacket() throws ByteArrayToBigToConvertException {
        ServerPacket packet = new ServerPacket();

        packet.addShort(chunkLength);
        packet.addByteArray(chunkData);
        packet.addByte(percentComplete);

        return packet;
    }
}
