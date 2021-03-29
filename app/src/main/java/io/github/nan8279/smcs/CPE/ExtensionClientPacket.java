package io.github.nan8279.smcs.CPE;

import io.github.nan8279.smcs.CPE.extensions.TwoWayPingExtension;
import io.github.nan8279.smcs.exceptions.InvalidPacketIDException;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;

/**
 * Lists all the client packets the extensions have.
 */
public enum ExtensionClientPacket {
    EXT_INFO_PACKET(16, new AbstractExtension.ClientExtInfoPacket(), 67),
    EXT_ENTRY_PACKET(17, new AbstractExtension.ClientExtEntryPacket(), 69),
    TWO_WAY_PING_PACKET(43, new TwoWayPingExtension.TwoWayPingClientPacket(), 3);

    final public int packetID;
    final public ClientBoundPacket packet;
    final public int packetLength;
    ExtensionClientPacket(int packetID, ClientBoundPacket packet, int packetLength) {
        this.packetID = packetID;
        this.packet = packet;
        this.packetLength = packetLength;
    }

    /**
     * Gets an extension packet from a packet ID.
     *
     * @param packetID the packet ID to look for.
     * @return the packet found.
     * @throws InvalidPacketIDException when the packet could not have been found.
     */
    public static ExtensionClientPacket getPacket(int packetID) throws InvalidPacketIDException {
        for (ExtensionClientPacket packet : ExtensionClientPacket.values()) {
            if (packet.packetID == packetID) {
                return packet;
            }
        }
        throw new InvalidPacketIDException();
    }
}
