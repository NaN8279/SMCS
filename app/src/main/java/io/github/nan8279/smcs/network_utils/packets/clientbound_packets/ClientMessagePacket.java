package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.Packet;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;

public class ClientMessagePacket implements ClientBoundPacket {
    private byte unusedByte;
    private String message;

    @Override
    public void fromPacket(Packet packet) throws InvalidPacketException {
        unusedByte = packet.readByte();
        message = packet.readString();
    }

    public String getMessage() {
        return message;
    }

    public byte getUnusedByte() {
        return unusedByte;
    }
}
