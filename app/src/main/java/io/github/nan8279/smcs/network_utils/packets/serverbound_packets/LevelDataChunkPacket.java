package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class LevelDataChunkPacket implements ServerBoundPacket {
    final private short chunkLength;
    final private byte[] chunkData;
    final private byte percentComplete;

    public LevelDataChunkPacket(short chunkLength, byte[] chunkData, byte percentComplete) throws ByteArrayToBigToConvertException {
        this.chunkLength = chunkLength;
        this.chunkData = NetworkUtils.generateByteArray(chunkData);
        this.percentComplete = percentComplete;
    }

    @Override
    public byte returnPacketID() {
        return 3;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add(NetworkUtils.shortToBytes(chunkLength)[0]);
        packet.add(NetworkUtils.shortToBytes(chunkLength)[1]);
        for (Byte b : chunkData) {
            packet.add(b);
        }
        packet.add(percentComplete);
        return packet;
    }
}
