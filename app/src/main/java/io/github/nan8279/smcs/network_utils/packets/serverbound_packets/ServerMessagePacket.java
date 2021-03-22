package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class ServerMessagePacket implements ServerBoundPacket {
    final private byte[] message;

    public ServerMessagePacket(String message) throws StringToBigToConvertException {
        this.message = NetworkUtils.generateString(message);
    }

    @Override
    public byte returnPacketID() {
        return 13;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add((byte) 0);

        for (Byte b : message) {
            packet.add(b);
        }
        return packet;
    }
}
