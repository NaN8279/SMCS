package io.github.nan8279.smcs.CPE;

import io.github.nan8279.smcs.CPE.extensions.TwoWayPingExtension;
import io.github.nan8279.smcs.exceptions.InvalidPacketIDException;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;

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

    public static ExtensionClientPacket getPacket(int packetID) throws InvalidPacketIDException {
        for (ExtensionClientPacket packet : ExtensionClientPacket.values()) {
            if (packet.packetID == packetID) {
                return packet;
            }
        }
        throw new InvalidPacketIDException();
    }
}
