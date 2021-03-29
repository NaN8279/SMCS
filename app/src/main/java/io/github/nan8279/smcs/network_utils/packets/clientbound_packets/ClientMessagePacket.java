package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.ClientPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;

/**
 * Client message packet.
 */
public class ClientMessagePacket implements ClientBoundPacket {
    private byte unusedByte;
    private String message;

    @Override
    public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
        unusedByte = clientPacket.readByte();
        message = clientPacket.readString();
    }

    /**
     * @return the message in the packet.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the unused byte in the packet.
     */
    public byte getUnusedByte() {
        return unusedByte;
    }
}
