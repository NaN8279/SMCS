package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Ping packet.
 */
public class PingPacket implements ServerBoundPacket {

    @Override
    public byte returnPacketID() {
        return 1;
    }

    @Override
    public ServerPacket returnPacket() {
        return new ServerPacket();
    }
}
