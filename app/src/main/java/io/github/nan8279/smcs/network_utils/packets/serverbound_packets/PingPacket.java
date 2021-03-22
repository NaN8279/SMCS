package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class PingPacket implements ServerBoundPacket {
    @Override
    public byte returnPacketID() {
        return 1;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        return new ArrayList<>();
    }
}
