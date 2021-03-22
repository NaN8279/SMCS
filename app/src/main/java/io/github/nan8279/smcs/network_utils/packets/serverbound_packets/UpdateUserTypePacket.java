package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class UpdateUserTypePacket implements ServerBoundPacket {
    final private boolean operator;

    public UpdateUserTypePacket(boolean operator) {
        this.operator = operator;
    }

    @Override
    public byte returnPacketID() {
        return 15;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add((byte) (operator ? 0x64: 0x00));
        return packet;
    }
}
