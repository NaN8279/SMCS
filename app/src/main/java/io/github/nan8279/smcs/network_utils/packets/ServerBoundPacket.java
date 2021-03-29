package io.github.nan8279.smcs.network_utils.packets;

import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;

/**
 * Server bound packet.
 */
public interface ServerBoundPacket {

    /**
     * @return the packet ID of this packet.
     */
    byte returnPacketID();

    /**
     * @return the raw data that needs to be send to the user.
     * @throws StringToBigToConvertException when a string in the packet is too big to send.
     * @throws ByteArrayToBigToConvertException when a byte array in the packet is too big to send.
     */
    ServerPacket returnPacket() throws StringToBigToConvertException, ByteArrayToBigToConvertException;
}
