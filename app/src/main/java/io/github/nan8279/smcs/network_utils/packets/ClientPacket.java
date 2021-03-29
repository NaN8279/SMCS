package io.github.nan8279.smcs.network_utils.packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketIDException;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientMessagePacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientPositionPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientSetBlockPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.PlayerIdentificationPacket;

/**
 * Lists client packets. (CPE client packets not included.)
 */
public enum ClientPacket {
    PLAYER_IDENTIFICATION_PACKET(0, new PlayerIdentificationPacket(), 131),
    SET_BLOCK_PACKET(5, new ClientSetBlockPacket(), 9),
    POSITION_PACKET(8, new ClientPositionPacket(), 10),
    MESSAGE_PACKET(13, new ClientMessagePacket(), 66);

    final public int packetID;
    final public ClientBoundPacket packet;
    final public int packetLength;
    ClientPacket(int packetID, ClientBoundPacket packet, int packetLength) {
        this.packetID = packetID;
        this.packet = packet;
        this.packetLength = packetLength;
    }

    /**
     * Returns a client packet object from a packet ID.
     *
     * @param packetID the packet ID to look for.
     * @return the packet.
     * @throws InvalidPacketIDException if the packet could not have been found.
     */
    public static ClientPacket getPacket(int packetID) throws InvalidPacketIDException {
        for (ClientPacket packet : ClientPacket.values()) {
            if (packet.packetID == packetID) {
                return packet;
            }
        }
        throw new InvalidPacketIDException();
    }
}
