package io.github.nan8279.smcs.network_utils.packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.ClientPacket;

/**
 * Client bound packet.
 */
public interface ClientBoundPacket {
    /**
     * Stores info from a packet into the packet object.
     *
     * @param clientPacket the packet to read from.
     * @throws InvalidPacketException if an invalid packet has been given.
     */
    void fromPacket(ClientPacket clientPacket) throws InvalidPacketException;
}
