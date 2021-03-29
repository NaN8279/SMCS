package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Server message packet.
 */
public class ServerMessagePacket implements ServerBoundPacket {
    final private String message;

    /**
     * @param message the message to send.
     */
    public ServerMessagePacket(String message) {
        this.message = message;
    }

    @Override
    public byte returnPacketID() {
        return 13;
    }

    @Override
    public ServerPacket returnPacket() throws StringToBigToConvertException {
        ServerPacket packet = new ServerPacket();

        packet.addByte((byte) 0);
        packet.addString(message);

        return packet;
    }
}
