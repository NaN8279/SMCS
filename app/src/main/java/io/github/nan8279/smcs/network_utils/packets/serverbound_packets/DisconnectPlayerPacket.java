package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class DisconnectPlayerPacket implements ServerBoundPacket {
    final private byte[] disconnectReason;

    public DisconnectPlayerPacket(String disconnectReason) throws StringToBigToConvertException {
        this.disconnectReason = NetworkUtils.generateString(disconnectReason);
    }

    @Override
    public byte returnPacketID() {
        return 14;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        for (Byte b : disconnectReason) {
            packet.add(b);
        }
        return packet;
    }
}
